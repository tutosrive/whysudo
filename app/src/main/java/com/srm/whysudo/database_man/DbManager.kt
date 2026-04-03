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
//import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
//import androidx.sqlite.driver.bundled.SQLITE_OPEN_READONLY
import androidx.sqlite.driver.bundled.SQLITE_OPEN_READWRITE
import javax.crypto.Cipher
import net.zetetic.database.sqlcipher.SQLiteDatabase

class DbManager(dbName: String, ctx: Context) {
    //    private val driver = BundledSQLiteDriver()
//    val conn =  driver.open(fileName = "${ctx.getDatabasePath(dbName).absolutePath}", SQLITE_OPEN_READONLY)
    var conn: SQLiteDatabase = SQLiteDatabase.openDatabase(
        "${ctx.getDatabasePath(dbName).absolutePath}",
        "1234",
        null, SQLiteDatabase.OPEN_READONLY,
        null
    )

}