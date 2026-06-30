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
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.srm.whysudo.markdown.MarkdownManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import androidx.core.net.toUri

class AllCommands : AppCompatActivity() {
    private lateinit var markManAll: MarkdownManager
    private lateinit var unlockPro: TextView
    private lateinit var btnBuyPro: Button
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
        markManAll = MarkdownManager(this)
        btnBuyPro = findViewById<Button>(R.id.buy_pro_btn)
        unlockPro = findViewById<TextView>(R.id.unlock_pro_home)
        this.btnBuyPro.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/details?id=com.whatsapp".toUri()
            )
            startActivity(intent)
        }
        this.setText()
    }

    fun setText() {
        mainScope.launch {
            val textUnlock: String = getString(R.string.unlock_pro_msg)
            markManAll.setMark(textUnlock, unlockPro)
        }
    }

    fun closeAllCommands(v: View): Unit {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}