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
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.srm.whysudo.utils.DataManager
import com.srm.whysudo.utils.MarkwonManager
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var inputCommandSearch: TextInputEditText
    private lateinit var commandInfoText: TextView
    private lateinit var markman: MarkwonManager
    private lateinit var dataman: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        markman = MarkwonManager(this)
        dataman = DataManager(this, "whysudo.srm")

        inputCommandSearch = findViewById<TextInputEditText>(R.id.searchInp)
        commandInfoText = findViewById<TextView>(R.id.infoTextMain)

        addListenerEvent()
    }

    private fun addListenerEvent(): Unit {
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
            var commandContent: String

            try {
                val fileObj: JSONObject = dataman.getJsonByKey(command)
                commandContent = dataman.getContentString(fileObj)
            } catch (error: JSONException) {
                commandContent = ""
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