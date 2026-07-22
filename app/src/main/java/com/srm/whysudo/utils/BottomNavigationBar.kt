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

package com.srm.whysudo.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.srm.whysudo.AllCommands
import com.srm.whysudo.BuildConfig
import com.srm.whysudo.MainActivity
import com.srm.whysudo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BottomNavigationBar(
    val ctx: Context,
    val dbMan: DBDataManager
) {
    val shPref: SharedSettings = SharedSettings(ctx)
    val activity: Activity = ctx as Activity
    val chipBottomNavigationBar: ChipNavigationBar = activity.findViewById(R.id.bottom_menu)
    val isPro: Boolean = BuildConfig.FLAVOR == "pro"

    init {
        this.setListeners()
        this.showBadgesInItem()
    }

    private fun setListeners(): Unit {
        // FIXME: There are a bug, load any ativity witout check that ARE in the same activity ...
        chipBottomNavigationBar.setOnItemSelectedListener { id ->
            val activityToLoad: Class<out AppCompatActivity> = when (id) {
                R.id.menu_favorites -> AllCommands::class.java
                R.id.menu_settings -> AllCommands::class.java
                R.id.menu_all_commands -> AllCommands::class.java
                else -> MainActivity::class.java
            }

            Utils.goToAnActivity(ctx, activityToLoad)
        }
    }

    private fun showBadgesInItem(): Unit {
        showBadgeFavorites()
        showBadgeAllCommands()
    }

    private fun showBadgeFavorites(): Unit {
        if (isPro) {
            CoroutineScope(Dispatchers.Main).launch {
                val count: Int = dbMan.getFavoritesCount()
                Log.i("showBadgeFavorites", "Favorites Count: $count")
                chipBottomNavigationBar.showBadge(R.id.menu_favorites, count)
            }
        }
    }

    private fun showBadgeAllCommands(): Unit {
        // FIXME: There are a bug unknown
        val isFirstOpen: Boolean = shPref.getPref("isFirstOpen", false)
        if (isFirstOpen) {
            CoroutineScope(Dispatchers.Main).launch {
                val count: Int = dbMan.getCommandsCount(isPro)
                Log.i("showBadgeAllCommands", "All Commands Count: $count")
                Log.i("showBadgeAllCommands", "Is Pro Count: $isPro")
                chipBottomNavigationBar.showBadge(R.id.menu_all_commands, count)
                shPref.savePref("isFirstOpen", true)
            }
        }
    }
}