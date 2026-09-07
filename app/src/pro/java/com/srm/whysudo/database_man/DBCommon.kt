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