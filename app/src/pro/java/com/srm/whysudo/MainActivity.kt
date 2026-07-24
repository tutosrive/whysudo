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

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.window.OnBackInvokedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.srm.whysudo.fragments.AllCommandsFragment
import com.srm.whysudo.fragments.HomeFragment
import com.srm.whysudo.models.CommandModelView
import com.srm.whysudo.utils.BottomNavigationBar
import com.srm.whysudo.utils.DBDataManager
import com.srm.whysudo.utils.Utils
import com.srm.whysudo.utils.Utils.hideKeyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private var commandClickedId by Delegates.notNull<Int>()
    private var currentCommand: CommandModelView? = null
    private lateinit var dbMan: DBDataManager
    val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var bottomBarManager: BottomNavigationBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        @Suppress("SourceLockedOrientationActivity")
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        Utils.loadLicensesFiles(this)
        dbMan = DBDataManager(ctx = this)

        bottomBarManager = BottomNavigationBar(this, dbMan, ::loadFragment)
        toggleFragment(homeFragment(), "HOME_F")
    }

    private fun toggleFragment(fragment: Fragment, tag: String): Unit {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, fragment, tag)
            .commit()
    }

    fun setCurrentCommand(command: CommandModelView): Unit {
        this.currentCommand = command
    }

    fun loadFragment(id: Int): Unit {
        val fragmentToLoad: Pair<String, Fragment> = when (id) {
            R.id.menu_favorites -> "HOME_F" to homeFragment()
            R.id.menu_settings -> "HOME_F" to homeFragment()
            R.id.menu_all_commands -> "ALL_COMMANDS_F" to allCommandsFragment()
            else -> "HOME_F" to homeFragment()
        }
        val existFragment: Fragment? = supportFragmentManager
            .findFragmentByTag(fragmentToLoad.first)

        if (existFragment == null) {
            toggleFragment(fragmentToLoad.second, fragmentToLoad.first)
        } else {
            Log.i("setListeners", "Is the same fragment: ${existFragment.tag}")
        }
    }

    private fun allCommandsFragment(): Fragment {
        return AllCommandsFragment(
            this, ::setCurrentCommand,
            bottomBarManager::updateCounters, bottomBarManager::clickNavigationBar
        )
    }

    private fun homeFragment(): HomeFragment {
        return HomeFragment(
            this, { hideKeyboard() },
            bottomBarManager::updateCounters, currentCommand
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        dbMan.close()
        mainScope.cancel()
    }

//    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {
//        val allFragments = supportFragmentManager.fragments
//        if (allFragments.isNotEmpty()) {
//            allFragments.forEach {
//                supportFragmentManager.beginTransaction().remove(it).commit()
//            }
//        }
//        Log.i("getOnBackInvokedDispatcher", "Back Pressed/Called")
//        return super.getOnBackInvokedDispatcher()
//    }
}