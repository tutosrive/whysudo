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