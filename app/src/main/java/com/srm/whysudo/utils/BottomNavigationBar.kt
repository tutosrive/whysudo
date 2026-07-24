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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.srm.whysudo.BuildConfig
import com.srm.whysudo.R
import com.srm.whysudo.fragments.HomeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

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
        this.saveInitialCounters().invokeOnCompletion {
            this.setListeners()
            this.showBadgesInItem()
        }
    }


    private fun saveInitialCounters(): Job {
        return CoroutineScope(Dispatchers.Main).launch {
            val favoritesCount: Int = dbMan.getFavoritesCount()
            val allCommandsCount: Int = dbMan.getCommandsCount(isPro)

            shPref.savePref("commandsCount", allCommandsCount.toFloat())
            shPref.savePref("favoritesCount", favoritesCount.toFloat())
        }

    }

    private fun setListeners(
    ): Unit {
        chipBottomNavigationBar.setOnItemSelectedListener { id ->
            loadFragment.invoke(id)
        }
    }

    private fun showBadgesInItem(): Unit {
        showBadgeFavorites()
        showBadgeAllCommands()
    }

    private fun showBadgeFavorites(): Unit {
        if (isPro) {
            val count = shPref.getPref("favoritesCount", 0f)
            Log.i("showBadgeFavorites", "Favorites Count: $count")
            chipBottomNavigationBar.showBadge(R.id.menu_favorites, count.toInt())
        }
    }

    private fun showBadgeAllCommands(): Unit {
        // FIXME: There are a bug unknown
//        val isFirstOpen: Boolean = shPref.getPref("isFirstOpen", false)
//        if (isFirstOpen) {
        val count = shPref.getPref("commandsCount", 0f)
        Log.i("showBadgeAllCommands", "All Commands Count: $count")
        Log.i("showBadgeAllCommands", "Is Pro Count: $isPro")
        chipBottomNavigationBar.showBadge(R.id.menu_all_commands, count.toInt())
//        shPref.savePref("isFirstOpen", true)
//        }
    }

}