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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class About : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_about)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadInfoAbout()
    }

    fun ctaClick(view: View) {
        // Just a joke!
        val infoText = findViewById<TextView>(R.id.infoText)
        infoText.text = "¿Por qué habrías de hacerlo?, un click en ¿un título?, ¿enserio?"
        lifecycleScope.launch {
            delay(1000)
            loadInfoAbout()
        }
    }

    private fun loadInfoAbout(): Unit {
        val infoText = findViewById<TextView>(R.id.infoText)
        val contentFileAbout = assets.open("about.srm").bufferedReader().use { it.readText() }
        infoText.text = contentFileAbout
    }

    fun close(view: View) {
        finish()
    }
}