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

package com.srm.whysudo.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.srm.whysudo.About
import com.srm.whysudo.R
import com.srm.whysudo.adapters.CustomRecyclerAdapter
import com.srm.whysudo.markdown.MarkdownManager
import com.srm.whysudo.models.CommandModelView
import com.srm.whysudo.utils.BtnDialog
import com.srm.whysudo.utils.ConfigDialog
import com.srm.whysudo.utils.CustomDialog
import com.srm.whysudo.utils.DBDataManager
import com.srm.whysudo.utils.ProUtils
import com.srm.whysudo.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
import kotlin.system.exitProcess

class HomeFragment(
    val ctx: Context,
    val hideKeyboard: () -> Unit,
    val updateBarBadges: () -> Unit,
    val currentCommand: CommandModelView? = null
) : Fragment() {
    private lateinit var dbDataManager: DBDataManager
    private var commandClickedId by Delegates.notNull<Int>()
    private lateinit var inputCommandSearch: TextInputEditText
    private lateinit var ctnInfoText: View
    private lateinit var commandInfoText: RecyclerView
    private lateinit var markman: MarkdownManager

    //    private lateinit var footerTxt: TextView
    private lateinit var commandHintView: TextView
    private lateinit var listCommands: RecyclerView
    private var commandsListValues: List<CommandModelView> = listOf()
    private lateinit var btnSeeAllCommands: ImageButton
    private lateinit var btnSetFavorite: ImageButton
    private val activity: Activity = ctx as Activity
    private lateinit var selfCycle: LifecycleCoroutineScope

    private lateinit var customAdapter: CustomRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selfCycle = viewLifecycleOwner.lifecycleScope
        getViews()
        loadFilesData()
        initialListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun getViews(): Unit {
        ctnInfoText = activity.findViewById<View>(R.id.ctnInfoText)
        inputCommandSearch = activity.findViewById<TextInputEditText>(R.id.searchInp)
        commandInfoText = activity.findViewById<RecyclerView>(R.id.infoTextMainR)
//        footerTxt = findViewById<TextView>(R.id.footerText)
        btnSeeAllCommands = activity.findViewById<ImageButton>(R.id.allCommandsBtn)
        btnSetFavorite = activity.findViewById<ImageButton>(R.id.btnFavorite)
        commandHintView = activity.findViewById<TextView>(R.id.commandHintDefault)
        listCommands = activity.findViewById<RecyclerView>(R.id.listCommands)
        listCommands.layoutManager = LinearLayoutManager(ctx)
        listCommands.itemAnimator = DefaultItemAnimator()

        customAdapter = CustomRecyclerAdapter(
            ctx,
            commandsListValues,
            ::handleFavoriteClick,
            ::handleListItemClick
        )

        listCommands.adapter = customAdapter
    }

    private fun loadFilesData(): Unit {
        markman = MarkdownManager(ctx)
        dbDataManager = DBDataManager(
            ctx = ctx,
            callbackOnStartLoad = { loadingStatus() },
            callbackOnFinishLoad = { addListenerEvent() },
            callbackOnError = { showDefaultError() }
        )
    }

    private fun initialListeners(): Unit {
//        btnSeeAllCommands.setOnClickListener { v -> goAllCommands() }
        btnSetFavorite.setOnClickListener { v ->
            setFavorite(commandClickedId)
            updateBarBadges.invoke()
        }
    }

    fun setFavorite(idCommand: Int): Unit {
        selfCycle.launch {
            val setFavoriteOk = dbDataManager.saveFavorite(idCommand)
            ProUtils.notifyFavoriteToView(btnSetFavorite, setFavoriteOk)
            ProUtils.showFavoriteSetToast(ctx, setFavoriteOk)
        }
    }

    private fun handleFavoriteClick(c: CommandModelView, v: ImageView): Unit {
        ProUtils.setCommandAsFavorite(ctx, dbDataManager, c, v)
        updateBarBadges.invoke()
    }

    private fun handleListItemClick(id: Int): Unit {
        selfCycle.launch {
            val commandSelected: CommandModelView = commandsListValues.find { it.id == id }!!
            commandClickedId = commandSelected.id
            showCommandContent(
                commandSelected.filename, commandSelected.isFavorite
            )
            hideKeyboard()
        }
    }

    fun loadingStatus(): Unit {
        selfCycle.launch {
            inputCommandSearch.isEnabled = false
            Utils.setViewVisibility(ctnInfoText, View.VISIBLE)
            markman.setMark(getString(R.string.loading_msg), commandHintView)
        }
    }

    private fun addListenerEvent(): Unit {
        selfCycle.launch {
            if (currentCommand == null) {
                showDefaultCommandHint()
            } else {
                commandClickedId = currentCommand.id
                showCommandContent(currentCommand.filename, currentCommand.isFavorite)
            }
            inputCommandSearch.isEnabled = true
            inputCommandSearch.hint = getString(R.string.input_search_main_hint)
            inputCommandSearch.doOnTextChanged { text, _, before, count ->
                changeRealTimeText(text, before, count)
            }
        }
    }

    fun showDefaultError(): Unit {
        selfCycle.launch {
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

//    private fun loadFooterDate() {
//        val footerStr: String = getString(R.string.footer_message)
//        Utils.setFooterContent(footerTxt, footerStr, markman)
//    }


    private fun changeRealTimeText(text: CharSequence?, before: Int, count: Int) {
        var fileNames: List<CommandModelView>? = null
        val command: String = (text ?: "").trim().toString()
        when {
            !command.isEmpty() -> selfCycle.launch {
                if (!command.isEmpty()) {
                    fileNames = getCommandData(command)
                }
                showCommandOrList(fileNames)
            }

            else -> {
                selfCycle.launch { showDefaultCommandHint() }
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
        val content = dbDataManager.getCommandContent(name)
        markman.setMark(content)
        ProUtils.notifyFavoriteToView(btnSetFavorite, isFavorite, false)
    }

    private fun showCommandList(list: List<CommandModelView>): Unit {
        commandsListValues = list
        customAdapter.setData(commandsListValues)
        Utils.setViewVisibility(ctnInfoText, View.GONE)
        Utils.setViewVisibility(listCommands, View.VISIBLE)
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
        ProUtils.notifyFavoriteToView(btnSetFavorite, isFavorite = false, animate = false)
    }

    private suspend fun getRandomCommand(): String {
        commandClickedId = Utils.getRandomIdInt()
        val command = dbDataManager.getCommandById(commandClickedId)
        return command
    }


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

    fun goAbout(): Unit {
        Utils.goToAnActivity(ctx, About::class.java)
    }

//    private fun goAllCommands(): Unit {
//        val intent = Intent(ctx, AllCommands::class.java)
//        intent.putExtra("previousActivity", "main")
//        waitResult.launch(intent)
//    }

    override fun onDestroy() {
        super.onDestroy()
        dbDataManager.close()
        selfCycle.cancel()
    }

//    companion object {
//        @JvmStatic
//        fun newInstance(ctx: Context, dbMan: DBDataManager) =
//            HomeFragment().apply {
//                arguments = Bundle().apply {
//                }
//            }
//    }
}