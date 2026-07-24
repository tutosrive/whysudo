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
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srm.whysudo.models.CommandModelView
import com.srm.whysudo.adapters.CustomRecyclerAdapter
import com.srm.whysudo.utils.BottomNavigationBar
import com.srm.whysudo.utils.DBDataManager
import com.srm.whysudo.utils.ProUtils
import com.srm.whysudo.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AllCommands : AppCompatActivity() {
    private var allCommands: List<CommandModelView> = listOf()
    private lateinit var dbMan: DBDataManager
    private lateinit var loadingL: LinearLayout
    private lateinit var errorL: LinearLayout
    private lateinit var viewListCommands: RecyclerView
    private lateinit var layoutAllCommands: LinearLayout
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var customAdapter: CustomRecyclerAdapter

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
        loadingL = findViewById<LinearLayout>(R.id.loadingLayout)
        errorL = findViewById<LinearLayout>(R.id.erroLayout)

        viewListCommands = findViewById<RecyclerView>(R.id.allCommandsList)
        viewListCommands.layoutManager = LinearLayoutManager(this@AllCommands)
        viewListCommands.itemAnimator = DefaultItemAnimator()
        customAdapter = CustomRecyclerAdapter(
            this@AllCommands,
            allCommands,
            ::handleFavoriteClick,
            { loadCommand(it, savedInstanceState) }
        )

        viewListCommands.adapter = customAdapter

        dbMan = DBDataManager(
            this,
            ::dataOnStartLoad,
            ::loadCommandsData
        )
//        BottomNavigationBar.init(this)
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
            allCommands = dbMan.getAllCommands()
            delay(50)
            dataOnFinishLoad()
        }
    }

    private fun loadCommand(id: Int, savedInstance: Bundle?): Unit {
        val previewsActivity = savedInstance?.getString("previousActivity")!!
        val commandSelected = allCommands.find { it.id == id }!!
        var result = Intent()
        if (previewsActivity != "main") {
            result = Intent(this, MainActivity::class.java)
        }
        mainScope.launch {
            result.putExtra("command", commandSelected.filename)
            result.putExtra("isFavorite", commandSelected.isFavorite)
            result.putExtra("idCommand", commandSelected.id)

            setResult(RESULT_OK, result)
            finish()
        }
    }

    private fun handleFavoriteClick(c: CommandModelView, v: ImageView): Unit {
        ProUtils.setCommandAsFavorite(this, dbMan, c, v)
    }

    private fun putData(): Unit {
        customAdapter.setData(allCommands)
        Utils.setViewVisibility(loadingL, View.GONE)
        Utils.setViewVisibility(errorL, View.GONE)
        Utils.setViewVisibility(layoutAllCommands, View.VISIBLE)

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