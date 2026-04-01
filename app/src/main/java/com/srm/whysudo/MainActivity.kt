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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
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
    private lateinit var ctnInfoText: View
    private lateinit var commandInfoText: TextView
    private lateinit var markman: MarkwonManager
    private lateinit var dbDataManager: DBDataManager
    private lateinit var footerTxt: TextView
    private lateinit var listCommands: ListView
    private lateinit var commandsListElements: List<String>
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

        ctnInfoText = findViewById<View>(R.id.ctnInfoText)
        inputCommandSearch = findViewById<TextInputEditText>(R.id.searchInp)
        commandInfoText = findViewById<TextView>(R.id.infoTextMain)
        footerTxt = findViewById<TextView>(R.id.footerText)
        listCommands = findViewById<ListView>(R.id.listCommands)
        markman = MarkwonManager(this)
        loadFooterDate()

        dbDataManager = DBDataManager(
            ctx = this,
            callbackOnStartLoad = { loadingStatus() },
            callbackOnFinishLoad = { addListenerEvent() }
        )
        listCommands.onItemClickListener = handleListItemClick()
    }

    private fun handleListItemClick(): AdapterView.OnItemClickListener {
        return AdapterView.OnItemClickListener { _, _, pos, _ ->
            mainScope.launch {
                val commandSelected = commandsListElements[pos]
                showCommandContent(commandSelected)
            }
        }
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
        inputCommandSearch.doOnTextChanged { text, _, before, count ->
            changeRealTimeText(text, before, count)
        }
    }

    private fun changeRealTimeText(text: CharSequence?, before: Int, count: Int) {
        var fileNames: List<String>? = null
        val command: String = (text ?: "").trim().toString()
        when {
            !command.isEmpty() -> mainScope.launch {
                if (!command.isEmpty()) {
                    fileNames = getCommandData(command)
                }
                showCommandOrList(fileNames)
            }

            else -> showDefaultCommandHint()
        }
    }

    private suspend fun getCommandData(command: String): List<String>? {
        var data: List<String>? = null
        try {
            data = dbDataManager.getFileNames(command)
        } catch (error: Exception) {
            showDefaultCommandHint()
        }
        return data
    }

    private suspend fun showCommandOrList(filenames: List<String>?): Unit {
        if (filenames == null) {
            showDefaultCommandHint()
            return
        }

        filenames.size.let {
            when (it) {
                1 -> showCommandContent(filenames[0])
                in 2..20 -> showCommandList(filenames)
                else -> showDefaultCommandHint()
            }
        }
    }

    private suspend fun showCommandContent(name: String): Unit {
        setViewVisibility(listCommands, View.INVISIBLE)
        setViewVisibility(ctnInfoText, View.VISIBLE)
        val content = dbDataManager.getCommandContent(name)
        markman.setMark(content, commandInfoText)
    }

    private suspend fun showCommandList(list: List<String>): Unit {
        commandsListElements = list
        setViewVisibility(ctnInfoText, View.INVISIBLE)
        setViewVisibility(listCommands, View.VISIBLE)

        val elements: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            list
        )

        listCommands.adapter = elements
    }

    private fun setViewVisibility(view: View, value: Int): Unit {
        val viewVisibility = view.visibility

        if (viewVisibility != value) {
            view.visibility = value
        }
    }

    private fun showDefaultCommandHint(): Unit {
        setViewVisibility(ctnInfoText, View.VISIBLE)
        setViewVisibility(listCommands, View.INVISIBLE)
        markman.setMark(getString(R.string.hint_main_info_command), commandInfoText)
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