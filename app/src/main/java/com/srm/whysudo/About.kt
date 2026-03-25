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

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.srm.whysudo.utils.DataManager
import com.srm.whysudo.utils.MarkwonManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class About : AppCompatActivity() {
    private lateinit var infoText: TextView
    private lateinit var markmanAbout: MarkwonManager
    private lateinit var datamanAbout: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_about)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        markmanAbout = MarkwonManager(this)
        datamanAbout = DataManager(this, "about.srm")

        infoText = findViewById<TextView>(R.id.infoText)
        loadInfoAbout()
    }

    fun ctaClick(view: View) {
        markmanAbout.setMark(
            "¿Por qué habrías de hacerlo?, un click en ¿un título?, ¿enserio?",
            infoText
        )
        lifecycleScope.launch {
            delay(1000)
            loadInfoAbout()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadInfoAbout() {
        var contentFileAbout = getString(R.string.loading_msg)
        markmanAbout.setMark(contentFileAbout, infoText)
        lifecycleScope.launch {
            delay(1000)
            contentFileAbout = try {
                datamanAbout.getContentString(datamanAbout.getData(), "en")
            } catch (error: Exception) {
                getString(R.string.try_reload_msg) + error.message.toString()
            }
            markmanAbout.setMark(contentFileAbout, infoText)
        }
    }

    fun close(view: View) {
        finish()
    }
}