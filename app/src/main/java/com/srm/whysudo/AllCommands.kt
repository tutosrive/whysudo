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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.srm.whysudo.utils.DBDataManager
import com.srm.whysudo.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AllCommands : AppCompatActivity() {
    private lateinit var allCommands: List<String>
    private lateinit var dbMan: DBDataManager
    private lateinit var loadingL: LinearLayout
    private lateinit var errorL: LinearLayout
    private lateinit var viewListCommands: ListView
    private lateinit var layoutAllCommands: LinearLayout
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_commands)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        layoutAllCommands = findViewById<LinearLayout>(R.id.commandsListLayout)
        viewListCommands = findViewById<ListView>(R.id.allCommandsList)
        loadingL = findViewById<LinearLayout>(R.id.loadingLayout)
        errorL = findViewById<LinearLayout>(R.id.erroLayout)

        dbMan = DBDataManager(this)

        viewListCommands.onItemClickListener = loadCommand()
        loadCommandsData()
    }

    private fun loadDataIntoView(): Unit {
        mainScope.launch {
            if (allCommands.isNotEmpty()) {
                putData()
            } else {
                Utils.setViewVisibility(errorL, View.VISIBLE)
            }
        }
    }

    private fun dataOnStartLoad(): Unit {
        Utils.setViewVisibility(loadingL, View.VISIBLE)
    }

    private fun dataOnFinishLoad(): Unit {
        loadDataIntoView()
    }

    private fun loadCommandsData(): Unit {
        mainScope.launch {
            dataOnStartLoad()
            allCommands = dbMan.getAllCommands()
            delay(300)
            dataOnFinishLoad()
        }
    }

    private fun loadCommand(): AdapterView.OnItemClickListener {
        return AdapterView.OnItemClickListener { _, _, pos, _ ->
            mainScope.launch {
                val commandSelected = allCommands[pos]
                val result = Intent()
                result.putExtra("command", commandSelected)

                setResult(RESULT_OK, result)
                finish()
            }
        }
    }

    private fun putData(): Unit {
        Utils.setViewVisibility(loadingL, View.GONE)
        Utils.setViewVisibility(errorL, View.GONE)
        Utils.setViewVisibility(layoutAllCommands, View.VISIBLE)

        val elements: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            allCommands
        )

        viewListCommands.adapter = elements
    }

    fun closeAllCommands(v: View): Unit {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbMan.close()
        mainScope.cancel()
    }
}