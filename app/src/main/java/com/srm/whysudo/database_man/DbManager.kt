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
import net.zetetic.database.sqlcipher.SQLiteDatabase

class DbManager(dbName: String, ctx: Context) {
    var conn: SQLiteDatabase = SQLiteDatabase.openDatabase(
        "${ctx.getDatabasePath(dbName).absolutePath}",
        "1234",
        null, SQLiteDatabase.OPEN_READONLY,
        null
    )

}