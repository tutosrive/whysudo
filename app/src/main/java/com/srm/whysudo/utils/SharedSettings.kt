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

import android.content.Context
import android.content.SharedPreferences
import com.srm.whysudo.enums.DataFileName
import androidx.core.content.edit

class SharedSettings(ctx: Context) {
    val sharedPref: SharedPreferences =
        ctx.getSharedPreferences(DataFileName.SHARED_FILE_NAME(), Context.MODE_PRIVATE)

    fun savePref(key: String, value: String) {
        this.sharedPref.edit {
            putString(key, value)
        }
    }

    fun savePref(key: String, value: Boolean) {
        this.sharedPref.edit {
            putBoolean(key, value)
        }
    }

    fun savePref(key: String, value: Float) {
        this.sharedPref.edit {
            putFloat(key, value)
        }
    }

    fun getPref(key: String, default: String?): String? {
        return sharedPref.getString(key, default)
    }

    fun getPref(key: String, default: Boolean = true): Boolean {
        return sharedPref.getBoolean(key, default)
    }

    fun getPref(key: String, default: Float = 0f): Float {
        return sharedPref.getFloat(key, default)
    }
}