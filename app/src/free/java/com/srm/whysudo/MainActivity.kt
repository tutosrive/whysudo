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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.srm.whysudo.utils.DBDataManager
import com.srm.whysudo.markdown.MarkdownManager
import com.srm.whysudo.utils.BtnDialog
import com.srm.whysudo.utils.ConfigDialog
import com.srm.whysudo.utils.CustomDialog
import com.srm.whysudo.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var inputCommandSearch: TextInputEditText
    private lateinit var ctnInfoText: View
    private lateinit var commandInfoText: TextView
    private lateinit var markman: MarkdownManager
    private lateinit var dbDataManager: DBDataManager
    private lateinit var footerTxt: TextView
    private lateinit var unlockProTextView: TextView
    private lateinit var listCommands: ListView
    private lateinit var commandsListValues: List<String>
    private lateinit var btnSeeAllCommands: ImageButton
    private lateinit var btnUnlockPro: Button
    private lateinit var layUnlockPro: LinearLayout

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
        unlockProTextView = findViewById<TextView>(R.id.unlock_pro_home)
        listCommands = findViewById<ListView>(R.id.listCommands)
        btnSeeAllCommands = findViewById<ImageButton>(R.id.allCommandsBtn)
        btnUnlockPro = findViewById<Button>(R.id.buy_pro_btn_home)
        layUnlockPro = findViewById<LinearLayout>(R.id.lay_unlock_pro_home)
        markman = MarkdownManager(this)
        loadFooterDate()

        dbDataManager = DBDataManager(
            ctx = this,
            callbackOnStartLoad = { loadingStatus() },
            callbackOnFinishLoad = { addListenerEvent() },
            callbackOnError = { showDefaultError() }
        )
        listCommands.onItemClickListener = handleListItemClick()
        btnSeeAllCommands.setOnClickListener { v -> goAllCommands(v) }
        Utils.loadLicensesFiles(this)
        btnUnlockPro.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                "https://my.play/tutosrive".toUri()
            )
            startActivity(intent)
        }
    }

    private fun handleListItemClick(): AdapterView.OnItemClickListener {
        return AdapterView.OnItemClickListener { _, _, pos, _ ->
            mainScope.launch {
                val commandSelected = commandsListValues[pos]
                if (isPro(commandSelected)) {
                    showUnlockPro(commandSelected)
                } else {
                    showCommandContent(commandSelected)
                }
            }
        }
    }

    fun loadingStatus(): Unit {
        mainScope.launch {
            inputCommandSearch.isEnabled = false
            Utils.setViewVisibility(ctnInfoText, View.VISIBLE)
            commandInfoText.text = getString(R.string.loading_msg)
        }
    }

    private fun loadFooterDate() {
        val footerStr: String = getString(R.string.footer_message)
        Utils.setFooterContent(footerTxt, footerStr, markman)
    }

    private fun addListenerEvent(): Unit {
        mainScope.launch {
            showDefaultCommandHint()
            inputCommandSearch.isEnabled = true

            inputCommandSearch.hint = getString(R.string.input_search_main_hint)
            inputCommandSearch.doOnTextChanged { text, _, before, count ->
                changeRealTimeText(text, before, count)
            }
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

            else -> {
                mainScope.launch { showDefaultCommandHint() }
            }
        }
    }

    private suspend fun getCommandData(command: String): List<String>? {
        var data: List<String>? = null
        try {
            data = dbDataManager.getFileNames(command)
        } catch (e: Exception) {
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
                1 -> {
                    if (isPro(filenames[0])) {
                        showUnlockPro(filenames[0])
                    } else {
                        showCommandContent(filenames[0])
                    }
                }

                in 2..it -> showCommandList(filenames)
                else -> showDefaultCommandHint()
            }
        }
    }

    private suspend fun showCommandContent(name: String): Unit {
        Utils.setViewVisibility(listCommands, View.INVISIBLE)
        Utils.setViewVisibility(ctnInfoText, View.VISIBLE)
        toggleUnlockPro(false)
        val content = dbDataManager.getCommandContent(name)
        markman.setMark(content, commandInfoText)
    }

    private suspend fun showCommandList(list: List<String>): Unit {
        commandsListValues = list
        Utils.setViewVisibility(ctnInfoText, View.GONE)
        Utils.setViewVisibility(listCommands, View.VISIBLE)

        val elements: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            list
        )

        listCommands.adapter = elements
    }

    private suspend fun showDefaultCommandHint(): Unit {
        Utils.setViewVisibility(ctnInfoText, View.VISIBLE)
        Utils.setViewVisibility(listCommands, View.INVISIBLE)
        toggleUnlockPro(false)
        val placeholderDefault = getString(R.string.hint_main_info_command)
        val msg = showRandomCommand(placeholderDefault)
        markman.setMark(msg, commandInfoText)
    }

    private suspend fun showRandomCommand(initialMessage: String): String {
        val id = Utils.getRandomIndexFree()
        val command = dbDataManager.getCommandById(id)
        val noteMsg = getString(R.string.default_command_note_header)
        val msg = "${initialMessage}\n---\n${noteMsg}\n---\n${command}"
        return msg
    }

    fun showDefaultError(): Unit {
        val ctx = this
        mainScope.launch {
            val msg = getString(R.string.general_error)
            val title = getString(R.string.error_dialog_title)
            val exit = { exitProcess(0) }
            val btnclose = BtnDialog(
                getString(R.string.btn_dialog_close), exit
            )
            val config = ConfigDialog(
                msg = msg,
                title = title,
                buttonClose = btnclose,
                callbackOnDismiss = exit
            )
            CustomDialog(ctx, config)
        }
    }

    fun showUnlockPro(command: String): Unit {
        toggleUnlockPro(true)
        val unlockText: String = getString(R.string.unlock_pro_msg_command)
            .replace("{thiscommand}", command)
        markman.setMark(unlockText, unlockProTextView)
    }

    fun toggleUnlockPro(show: Boolean): Unit {
        val visible: Int = if (show) View.VISIBLE else View.GONE
        val gone: Int = if (show) View.GONE else View.VISIBLE
        Utils.setViewVisibility(btnUnlockPro, visible)
        Utils.setViewVisibility(layUnlockPro, visible)
        Utils.setViewVisibility(commandInfoText, gone)
    }

    fun isPro(command: String): Boolean {
        return command.contains(" - PRO")
    }

    fun goAbout(v: View): Unit {
        Utils.goToAnActivity(this, v, About::class.java)
    }

    private fun goAllCommands(v: View): Unit {
        Utils.goToAnActivity(this, v, AllCommands::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        dbDataManager.close()
        mainScope.cancel()
    }
}