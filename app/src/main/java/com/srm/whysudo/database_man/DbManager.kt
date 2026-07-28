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

package com.srm.whysudo.database_man

import android.content.Context
import com.srm.whysudo.utils.SharedSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.zetetic.database.sqlcipher.SQLiteConnection
import net.zetetic.database.sqlcipher.SQLiteDatabase
import net.zetetic.database.sqlcipher.SQLiteDatabaseHook

class DbManager(dbName: String, ctx: Context, qr: String) {
    val shPref: SharedSettings = SharedSettings(ctx)
    var conn: SQLiteDatabase
    val hook = object : SQLiteDatabaseHook {
        override fun preKey(p0: SQLiteConnection?) {}

        override fun postKey(p0: SQLiteConnection?) {
            p0?.execute("PRAGMA kdf_iter = 5000;", null, null)
        }
    }

    init {
        System.loadLibrary("sqlcipher")
        conn = SQLiteDatabase.openDatabase(
            "${ctx.getDatabasePath(dbName).absolutePath}",
            qr,
            null, SQLiteDatabase.OPEN_READWRITE,
            hook
        )
//        handleStarter()
    }

    fun close() {
        conn.close()
    }

    suspend fun handleStarter(): Job? {
//      return  CoroutineScope(Dispatchers.IO).launch {
        var job: Job? = null
        val isFavoriteColCreated: Boolean = shPref.getPref(
            "favorite_col_created", false
        )
        if (!isFavoriteColCreated) {
            job = DbStarter(conn).startDb()
//                DbStarter(conn).startDb().invokeOnCompletion {
//                    shPref.savePref("favorite_col_created", true)
//                }
        }

        return job
//        }
    }
}