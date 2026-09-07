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

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
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

class MainActivity : AppCompatActivity() {
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
        dbMan = DBDataManager(ctx = this, callbackOnFinishLoad = ::loadStartState)
    }

    private fun loadStartState(): Unit {
        bottomBarManager = BottomNavigationBar(this, dbMan, ::loadFragment)
//        bottomBarManager.clickNavigationBar(R.id.menu_home)
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
            R.id.menu_favorites -> "FAVORITES_F" to favoriteCommandsFragment()
            R.id.menu_settings -> "HOME_F" to homeFragment()
            R.id.menu_all_commands -> "ALL_COMMANDS_F" to allCommandsFragment()
            else -> "HOME_F" to homeFragment()
        }
        val existFragment: Fragment? = supportFragmentManager
            .findFragmentByTag(fragmentToLoad.first)

        if (existFragment == null) {
//            supportFragmentManager.beginTransaction().remove(existFragment).commit()
            toggleFragment(fragmentToLoad.second, fragmentToLoad.first)
        } else {
            Log.i("setListeners", "Is the same fragment: ${existFragment.tag}")
        }
    }

    private fun allCommandsFragment(): Fragment {
        return AllCommandsFragment(
            this, bottomBarManager::updateCounters,
            bottomBarManager::clickNavigationBar, ::setCurrentCommand
        )
    }

    private fun favoriteCommandsFragment(): Fragment {
        return AllCommandsFragment(
            this, bottomBarManager::updateCounters,
            bottomBarManager::clickNavigationBar, ::setCurrentCommand, true
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
}