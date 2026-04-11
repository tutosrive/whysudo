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
import net.zetetic.database.sqlcipher.SQLiteConnection
import net.zetetic.database.sqlcipher.SQLiteDatabase
import net.zetetic.database.sqlcipher.SQLiteDatabaseHook

class DbManager(dbName: String, ctx: Context) {
    var conn: SQLiteDatabase

    init {
        System.loadLibrary("sqlcipher")
        conn = SQLiteDatabase.openDatabase(
            "${ctx.getDatabasePath(dbName).absolutePath}",
            $$"$argon2id$v=19$m=19456,t=17,p=4$YmZmMTFkODAyMjQxYjA0NzI2Y2Q0MWU5NzU2MmVlNDU$tiW78vf8mwKwV7HFq1yhVJmJy4dxBkXcH0KHTEqO5fxRXTX3JxqBokdc286cC0CFElKlDjCPn3LrUXLLM78Fww",
            null, SQLiteDatabase.OPEN_READONLY,
            H
        )
    }

    fun close() {
        conn.close()
    }

    object H : SQLiteDatabaseHook {
        override fun preKey(p0: SQLiteConnection?) {}

        override fun postKey(p0: SQLiteConnection?) {
            p0?.execute("PRAGMA kdf_iter = 5000;", null, null)
        }
    }
}