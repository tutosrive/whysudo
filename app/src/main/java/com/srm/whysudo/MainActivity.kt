/*
 * Copyright (c) 2026 tutosrive. All rights reserved.
 *
 * Author: tutosrive
 * GitHub: https://github.com/tutosrive
 *
 * This source code is PROPRIETARY and CONFIDENTIAL.
 * Unauthorized copying, modification, or distribution of this file,
 * via any medium, is strictly prohibited.
 *
 * This software is provided "as is", without warranty of any kind.
 * In no event shall the author be liable for any claim or damages.
 */

package com.srm.whysudo

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.srm.whysudo.utils.DBDataManager
import com.srm.whysudo.utils.MarkwonManager
import com.srm.whysudo.utils.Utils
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var inputCommandSearch: TextInputEditText
    private lateinit var commandInfoText: TextView
    private lateinit var markman: MarkwonManager
    private lateinit var dbDataManager: DBDataManager
    private lateinit var footerTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        inputCommandSearch = findViewById<TextInputEditText>(R.id.searchInp)
        commandInfoText = findViewById<TextView>(R.id.infoTextMain)
        footerTxt = findViewById<TextView>(R.id.footerText)
        markman = MarkwonManager(this)
        loadFooterDate()

        dbDataManager = DBDataManager(this, { loadingStatus() }, { addListenerEvent() })
    }

    fun loadingStatus(): Unit {
        commandInfoText.text = getString(R.string.loading_msg)
        inputCommandSearch.isEnabled = false
        inputCommandSearch.hint = "Loading Database..."
    }

    private fun loadFooterDate() {
        val footerStr: String = getString(R.string.footer_message)
        Utils.setFooterContent(footerTxt, footerStr, markman)
    }

    private fun addListenerEvent(): Unit {
        commandInfoText.text = getString(R.string.hint_main_info_command)
        inputCommandSearch.isEnabled = true
        inputCommandSearch.hint = getString(R.string.input_search_main_hint)
        inputCommandSearch.doOnTextChanged { text, start, before, count ->
            changeRealTimeText(
                text,
                start,
                before,
                count
            )
        }
    }

    private fun changeRealTimeText(text: CharSequence?, start: Int, before: Int, count: Int) {
        val command: String = (text ?: "").trim().toString()

        if (!command.isEmpty()) {
            var commandContent: String = ""
            try {
                lifecycleScope.launch {
                    commandContent += dbDataManager.dbGetCommandContent(command)
                }
            } catch (error: Exception) {
                commandContent += getString(R.string.hint_main_info_command)
            }
            markman.setMark(commandContent, commandInfoText)
        }
    }

    @Suppress("Unused")
    fun goAbout(view: View): Unit {
        val intent = Intent(this, About::class.java)
        startActivity(intent)
    }
}