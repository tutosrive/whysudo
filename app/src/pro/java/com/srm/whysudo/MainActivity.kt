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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.srm.whysudo.fragments.HomeFragment
import com.srm.whysudo.markdown.MarkdownManager
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
    private lateinit var dbMan: DBDataManager
    val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

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

        toggleFragment(homeFragment(), "HOME_F")
        BottomNavigationBar(this, dbMan, ::loadFragment)
    }

    private fun toggleFragment(fragment: Fragment, tag: String): Unit {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, fragment, tag)
            .commit()
    }

    fun loadFragment(id: Int): Unit {
        val fragmentToLoad: Pair<String, HomeFragment> = when (id) {
            R.id.menu_favorites -> "HOME_F" to homeFragment()
            R.id.menu_settings -> "HOME_F" to homeFragment()
            R.id.menu_all_commands -> "HOME_F" to homeFragment()
            else -> "HOME_F" to homeFragment()
        }
        val currentFragment: Fragment? = supportFragmentManager
            .findFragmentByTag(fragmentToLoad.first)

        if (
            currentFragment != null &&
            currentFragment::class.java != fragmentToLoad.second::class.java
        ) {
            toggleFragment(fragmentToLoad.second, fragmentToLoad.first)
        } else {
            Log.i("setListeners", "Is the same fragment: ${currentFragment?.tag}")
        }
    }

    private fun homeFragment(): HomeFragment {
        return HomeFragment(this, mainScope, { hideKeyboard() })
    }

    override fun onDestroy() {
        super.onDestroy()
        dbMan.close()
        mainScope.cancel()
    }
}