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
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.srm.whysudo.utils.DBDataManager
import com.srm.whysudo.utils.MarkwonManager
import com.srm.whysudo.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var inputCommandSearch: TextInputEditText
    private lateinit var commandInfoText: TextView
    private lateinit var markman: MarkwonManager
    private lateinit var dbDataManager: DBDataManager
    private lateinit var footerTxt: TextView
    private val tag: String? = this::class.simpleName
    val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        @Suppress("SourceLockedOrientationActivity")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        inputCommandSearch = findViewById<TextInputEditText>(R.id.searchInp)
        commandInfoText = findViewById<TextView>(R.id.infoTextMain)
        footerTxt = findViewById<TextView>(R.id.footerText)
        markman = MarkwonManager(this)
        loadFooterDate()

        dbDataManager = DBDataManager(this, { loadingStatus() }, { addListenerEvent() })
    }

    fun loadingStatus(): Unit {
        inputCommandSearch.isEnabled = false
    }

    private fun loadFooterDate() {
        val footerStr: String = getString(R.string.footer_message)
        Utils.setFooterContent(footerTxt, footerStr, markman)
    }

    private fun addListenerEvent(): Unit {
        commandInfoText.text = getString(R.string.hint_main_info_command)
        inputCommandSearch.isEnabled = true

        inputCommandSearch.hint = getString(R.string.input_search_main_hint)
        inputCommandSearch.doOnTextChanged { text, _, _, _ -> changeRealTimeText(text) }
    }

    private fun changeRealTimeText(text: CharSequence?) {
        mainScope.launch {
            val command: String = (text ?: "").trim().toString()
            var msg: String
            if (!command.isEmpty()) {
                val content = getCommandData(command)
                msg = content.toString()
            } else {
                msg = getString(R.string.hint_main_info_command)
            }
            markman.setMark(msg, commandInfoText)
        }
    }

    private suspend fun getCommandData(command: String): List<String> {
        var data: List<String>
        try {
            data = dbDataManager.dbGetCommandContent(command)
            Log.i(tag, data.toString())
        } catch (error: Exception) {
            data = listOf("${getString(R.string.hint_main_info_command)} -> ${error.message}")
        }
        return data
    }

    @Suppress("Unused")
    fun goAbout(view: View): Unit {
        val intent = Intent(this, About::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}