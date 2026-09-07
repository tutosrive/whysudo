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

package com.srm.whysudo.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.srm.whysudo.BuildConfig
import com.srm.whysudo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BottomNavigationBar(
    val ctx: Context,
    val dbMan: DBDataManager,
    val loadFragment: (id: Int) -> Unit
) {

    private val shPref: SharedSettings = SharedSettings(ctx)
    private val activity = ctx as Activity
    private val chipBottomNavigationBar: ChipNavigationBar = activity.findViewById(R.id.bottom_menu)
    val isPro: Boolean = BuildConfig.FLAVOR == "pro"

    init {
        this.saveCounters().invokeOnCompletion {
            this.setListeners()
            this.showBadgesInItem()
        }
    }


    private fun saveCounters(): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            val favoritesCount: Int = dbMan.getFavoritesCount()
            val allCommandsCount: Int = dbMan.getCommandsCount(isPro)

            shPref.savePref("commandsCount", allCommandsCount.toFloat())
            shPref.savePref("favoritesCount", favoritesCount.toFloat())
        }
    }

    fun updateCounters(): Unit {
        this.saveCounters().invokeOnCompletion {
            showBadgeFavorites()
        }
    }

    private fun setListeners(
    ): Unit {
        chipBottomNavigationBar.setOnItemSelectedListener { id ->
            loadFragment.invoke(id)
        }
    }

    fun clickNavigationBar(id: Int): Unit {
        chipBottomNavigationBar.setItemSelected(id, isSelected = true, dispatchAction = true)
    }

    private fun showBadgesInItem(): Unit {
        showBadgeFavorites()
        showBadgeAllCommands()
    }

    private fun showBadgeFavorites(): Unit {
        if (isPro) {
            val count = shPref.getPref("favoritesCount", 0f)
            chipBottomNavigationBar.showBadge(R.id.menu_favorites, count.toInt())
            CoroutineScope(Dispatchers.Main).launch {
                dismissBadge(R.id.menu_favorites)
            }
        }
    }

    private fun showBadgeAllCommands(): Unit {
        val count = shPref.getPref("commandsCount", 0f)
        chipBottomNavigationBar.showBadge(R.id.menu_all_commands, count.toInt())
        CoroutineScope(Dispatchers.Main).launch {
            dismissBadge(R.id.menu_all_commands)
        }
    }

    private suspend fun dismissBadge(id: Int): Unit {
        delay(2000)
        chipBottomNavigationBar.dismissBadge(id)
    }
}