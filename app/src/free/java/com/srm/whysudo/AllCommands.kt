/*
 * Copyright (c) 2026 tutosrive
 *
 * Author: tutosrive
 * GitHub: https://github.com/tutosrive
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
                "https://my.play/tutosrive".toUri()
            )
            startActivity(intent)
        }
        this.setText()
    }

    fun setText() {
        mainScope.launch {
            val propertyCommand = getString(R.string.property_functionality)
            val textUnlock: String = getString(R.string.unlock_pro_msg_command)
                .replace("{property}", propertyCommand)
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