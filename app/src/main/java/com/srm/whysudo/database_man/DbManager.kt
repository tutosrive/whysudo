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
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.zetetic.database.sqlcipher.SQLiteConnection
import net.zetetic.database.sqlcipher.SQLiteDatabase
import net.zetetic.database.sqlcipher.SQLiteDatabaseHook
import kotlin.collections.contains

class DbManager(dbName: String, ctx: Context, qr: String) {
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
        handleStarter()
    }

    fun close() {
        conn.close()
    }


    private fun handleStarter(): Unit {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cols = conn.rawQuery("SELECT * FROM file limit 1")
                    .columnNames

                if (!cols.contains("is_favorite")) {
                    Log.i(this::class.java.simpleName, "Cols don't contains is_favorite")
                    DbStarter(conn).startDb().invokeOnCompletion()
                    {
                        val a = conn.rawQuery("SELECT * FROM file limit 1")
                            .columnNames

                        Log.i(this::class.java.simpleName, a.joinToString(","))
                        Log.i(
                            this::class.java.simpleName,
                            "Line after first select, inside invokeCompletion"
                        )
                    }
                } else {
                    Log.i(this::class.java.simpleName, "Favorite column EXIST")
                    Log.i(this::class.java.simpleName, cols.joinToString(","))
                }
            } catch (e: Exception) {
                Log.i(this::class.java.simpleName, e.message.toString())
            }
        }
    }
}