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

import net.zetetic.database.sqlcipher.SQLiteDatabase
import com.srm.whysudo.interfaces.DBCommon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DbStarter(val db: SQLiteDatabase) : DBCommon {
    override suspend fun startDb(): Job {
        return createFavoriteTable()
    }

    private fun createFavoriteTable(): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            val query: String = """
                ALTER TABLE file ADD COLUMN is_favorite INTEGER NOT NULL DEFAULT 0;
            """.trimIndent()

//            db.rawQuery(query, null).close()
            db.execSQL(query)
//            db.rawExecSQL()
        }
    }
}