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
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.srm.whysudo.adapters.CommandModelView
import com.srm.whysudo.adapters.CustomListAdapter
import com.srm.whysudo.markdown.MarkdownManager
import com.srm.whysudo.utils.BtnDialog
import com.srm.whysudo.utils.ConfigDialog
import com.srm.whysudo.utils.CustomDialog
import com.srm.whysudo.utils.DBDataManager
import com.srm.whysudo.utils.ProUtils
import com.srm.whysudo.utils.Utils
import com.srm.whysudo.utils.Utils.hideKeyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private var commandClickedId by Delegates.notNull<Int>()
    private lateinit var inputCommandSearch: TextInputEditText
    private lateinit var ctnInfoText: View
    private lateinit var commandInfoText: RecyclerView
    private lateinit var markman: MarkdownManager
    private lateinit var dbDataManager: DBDataManager
    private lateinit var footerTxt: TextView
    private lateinit var commandHintView: TextView
    private lateinit var listCommands: ListView
    private lateinit var commandsListValues: List<CommandModelView>
    private lateinit var btnSeeAllCommands: ImageButton
    private lateinit var btnSetFavorite: ImageButton
    private val waitResult = waitAllcomandsResult()

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
        getViews()
        loadFilesData()
        initialListeners()
    }

    private fun getViews(): Unit {
        ctnInfoText = findViewById<View>(R.id.ctnInfoText)
        inputCommandSearch = findViewById<TextInputEditText>(R.id.searchInp)
        commandInfoText = findViewById<RecyclerView>(R.id.infoTextMainR)
        footerTxt = findViewById<TextView>(R.id.footerText)
        listCommands = findViewById<ListView>(R.id.listCommands)
        btnSeeAllCommands = findViewById<ImageButton>(R.id.allCommandsBtn)
        btnSetFavorite = findViewById<ImageButton>(R.id.btnFavorite)
        commandHintView = findViewById<TextView>(R.id.commandHintDefault)
    }

    private fun loadFilesData(): Unit {
        markman = MarkdownManager(this)
        loadFooterDate()

        dbDataManager = DBDataManager(
            ctx = this,
            callbackOnStartLoad = { loadingStatus() },
            callbackOnFinishLoad = { addListenerEvent() },
            callbackOnError = { showDefaultError() }
        )
        listCommands.onItemClickListener = handleListItemClick()
        Utils.loadLicensesFiles(this)
    }

    private fun initialListeners(): Unit {
        btnSeeAllCommands.setOnClickListener { v -> goAllCommands(v) }
        btnSetFavorite.setOnClickListener { v ->
            setFavorite(commandClickedId)
        }
    }

    fun setFavorite(idCommand: Int): Unit {
        mainScope.launch {
            val setFavoriteOk = dbDataManager.saveFavorite(idCommand)
            if (setFavoriteOk) {
                ProUtils.notifyFavoriteToView(btnSetFavorite, true)
            } else {
                val msg = getString(R.string.msg_favorite_error)
                Toast.makeText(
                    this@MainActivity,
                    msg,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun handleListItemClick(): AdapterView.OnItemClickListener {
        return AdapterView.OnItemClickListener { _, _, pos, _ ->
            mainScope.launch {
                val commandSelected = commandsListValues[pos]
                commandClickedId = commandSelected.id
                showCommandContent(
                    commandSelected.filename, commandSelected.isFavorite
                )
                hideKeyboard()
            }
        }
    }

    fun loadingStatus(): Unit {
        mainScope.launch {
            inputCommandSearch.isEnabled = false
            Utils.setViewVisibility(ctnInfoText, View.VISIBLE)
            markman.setMark(getString(R.string.loading_msg), commandHintView)
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
        var fileNames: List<CommandModelView>? = null
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

    private suspend fun getCommandData(command: String): List<CommandModelView>? {
        var data: List<CommandModelView>? = null
        try {
            data = dbDataManager.getFileNames(command)
        } catch (e: Exception) {
            showDefaultCommandHint()
        }
        return data
    }

    private suspend fun showCommandOrList(filenames: List<CommandModelView>?): Unit {
        if (filenames == null) {
            showDefaultCommandHint()
            return
        }

        filenames.size.let {
            when (it) {
                1 -> {
                    val selected = filenames[0]
                    showCommandContent(selected.filename, selected.isFavorite)
                }

                in 2..it -> showCommandList(filenames)
                else -> showDefaultCommandHint()
            }
        }
    }

    private suspend fun showCommandContent(name: String, isFavorite: Boolean): Unit {
        Utils.setViewVisibility(listCommands, View.INVISIBLE)
        Utils.setViewVisibility(ctnInfoText, View.VISIBLE)
        Utils.setViewVisibility(commandHintView, View.GONE)

//        notifyFavoriteToView(isFavorite)
        ProUtils.notifyFavoriteToView(btnSetFavorite, isFavorite)
        val content = dbDataManager.getCommandContent(name)
        markman.setMark(content)
    }

    private fun showCommandList(list: List<CommandModelView>): Unit {
        commandsListValues = list
        Utils.setViewVisibility(ctnInfoText, View.GONE)
        Utils.setViewVisibility(listCommands, View.VISIBLE)

        val elements: CustomListAdapter = CustomListAdapter(
            this,
            list,
            dbDataManager
        )

        listCommands.adapter = elements
    }

    private suspend fun showDefaultCommandHint(): Unit {
        Utils.setViewVisibility(ctnInfoText, View.VISIBLE)
        Utils.setViewVisibility(listCommands, View.INVISIBLE)
        Utils.setViewVisibility(commandHintView, View.VISIBLE)
        val placeholderDefault = getString(R.string.hint_main_info_command)
        val noteMsg = getString(R.string.default_command_note_header)
        val msg = "${placeholderDefault}\n---\n${noteMsg}\n---\n"
        val randomCommand = getRandomCommand()
        markman.setMark(msg, commandHintView)
        markman.setMark(randomCommand)
    }

    private suspend fun getRandomCommand(): String {
        commandClickedId = Utils.getRandomIdInt()
        val command = dbDataManager.getCommandById(commandClickedId)
        return command
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

//    private fun notifyFavoriteToView(isFavorite: Boolean): Unit {
//        when (isFavorite) {
//            true -> btnSetFavorite.setImageResource(R.drawable.ic_star_filled)
//            false -> btnSetFavorite.setImageResource(R.drawable.ic_star)
//        }
//    }

    private fun waitAllcomandsResult(): ActivityResultLauncher<Intent?> {
        return registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val command = data?.getStringExtra("command")
                val favorite = data?.getBooleanExtra("isFavorite", false) ?: false
                commandClickedId = data?.getIntExtra("idCommand", -1)!!
                lifecycleScope.launch {
                    showCommandContent("$command", favorite)
                }
            }
        }
    }

    fun goAbout(v: View): Unit {
        Utils.goToAnActivity(this, v, About::class.java)
    }

    private fun goAllCommands(v: View): Unit {
        val intent = Intent(this, AllCommands::class.java)
        waitResult.launch(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        dbDataManager.close()
        mainScope.cancel()
    }
}