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
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srm.whysudo.R
import com.srm.whysudo.adapters.CustomRecyclerAdapter
import com.srm.whysudo.models.CommandModelView
import com.srm.whysudo.utils.DBDataManager
import com.srm.whysudo.utils.ProUtils
import com.srm.whysudo.utils.Utils
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AllCommandsFragment(
    val ctx: Context,
    val updateBarBadges: () -> Unit,
    val loadFragment: (id: Int) -> Unit,
    val setCommand: ((c: CommandModelView) -> Unit),
    val isLoadFavorites: Boolean = false
) : Fragment() {
    private var allCommands: List<CommandModelView> = listOf()
    private lateinit var dbMan: DBDataManager
    private lateinit var loadingL: LinearLayout
    private lateinit var errorL: LinearLayout
    private lateinit var viewListCommands: RecyclerView
    private lateinit var layoutAllCommands: LinearLayout
    private lateinit var backBtn: ImageButton
    private lateinit var titleFragment: TextView
    private lateinit var errorView: TextView
    private lateinit var customAdapter: CustomRecyclerAdapter
    private lateinit var selfCycle: LifecycleCoroutineScope

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_commands, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selfCycle = viewLifecycleOwner.lifecycleScope
        getViews(view)
        setTitle()
        dbMan = DBDataManager(
            ctx,
            ::dataOnStartLoad,
            ::loadCommandsData
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        dbMan.close()
    }

    private fun setTitle(): Unit {
        val titleId: Int = when (isLoadFavorites) {
            true -> R.string.title_all_commands_activity
            false -> R.string.title_favorites_f
        }
        titleFragment.text = getString(titleId)
    }

    private fun loadDataIntoView(): Unit {
        selfCycle.launch {
            if (allCommands.isNotEmpty()) {
                putData()
            } else {
                Utils.setViewVisibility(errorL, View.VISIBLE)
                errorView.text = getString(R.string.msg_favorites_null_f)
            }
        }
    }

    private fun getViews(v: View): Unit {
        layoutAllCommands = v.findViewById<LinearLayout>(R.id.commandsListLayout)
        loadingL = v.findViewById<LinearLayout>(R.id.loadingLayout)
        errorL = v.findViewById<LinearLayout>(R.id.erroLayout)
        backBtn = v.findViewById<ImageButton>(R.id.backBtn)
        titleFragment = v.findViewById<TextView>(R.id.allCommandsTitle)
        errorView = v.findViewById<TextView>(R.id.errorTextView)

        backBtn.setOnClickListener { close() }

        viewListCommands = v.findViewById<RecyclerView>(R.id.allCommandsList)
        viewListCommands.layoutManager = LinearLayoutManager(ctx)
        viewListCommands.itemAnimator = DefaultItemAnimator()
        customAdapter = CustomRecyclerAdapter(
            ctx,
            allCommands,
            ::handleFavoriteClick,
            ::loadCommand
        )

        viewListCommands.adapter = customAdapter
    }

    private fun dataOnStartLoad(): Unit {
        Utils.setViewVisibility(loadingL, View.VISIBLE)
    }

    private fun dataOnFinishLoad(): Unit {
        loadDataIntoView()
    }

    private fun loadCommandsData(): Unit {
        selfCycle.launch {
            allCommands = when (isLoadFavorites) {
                true -> dbMan.getFavoritesCommands()
                false -> dbMan.getAllCommands()
            }
//            delay(50)
            dataOnFinishLoad()
        }
    }

    private fun loadCommand(id: Int): Unit {
        val commandSelected = allCommands.find { it.id == id }!!
        setCommand.invoke(commandSelected)
        Log.i("loadCommand", commandSelected.filename)
        close()
    }

    private fun handleFavoriteClick(c: CommandModelView, v: ImageView): Unit {
        // Would to use a general function on MainAcivity, and in this fragment, just call to
        // "setCommand(c)" and the callback from Main ... like this: "handleFavorite.invoke(v)"
        // would be because MainActivity has ctx and a dbMan ... just sent the command "c" and view "v"
        ProUtils.setCommandAsFavorite(ctx, dbMan, c, v).invokeOnCompletion {
            updateBarBadges.invoke()
        }
    }

    private fun putData(): Unit {
        customAdapter.setData(allCommands)
        Utils.setViewVisibility(loadingL, View.GONE)
        Utils.setViewVisibility(errorL, View.GONE)
        Utils.setViewVisibility(layoutAllCommands, View.VISIBLE)

    }

    fun close(): Unit {
        loadFragment.invoke(R.id.menu_home)
    }

    override fun onDestroy() {
        super.onDestroy()
        dbMan.close()
//        parentFragmentManager.beginTransaction().remove(this).commit()
    }
}