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
import android.util.Log
import androidx.sqlite.SQLiteConnection
import com.srm.whysudo.database_man.DbManager
import com.srm.whysudo.enums.DataFileName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DBDataManager(
    private val ctx: Context,
    val callbackOnStartLoad: () -> Unit,
    val callbackOnFinishLoad: () -> Unit
) {
    val fileDataName: String = DataFileName.DB_COMMANDS()
    private lateinit var dbMan: DbManager
    private lateinit var db: SQLiteConnection

    init {
        loadData()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadData() {
        val job = GlobalScope.launch {
            callbackOnStartLoad()
            Utils.copyFileFromAssets(ctx = ctx, filename = fileDataName, isDb = true)
        }

        job.invokeOnCompletion {
            dbMan = DbManager(fileDataName, ctx)
            db = dbMan.conn
            callbackOnFinishLoad()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Throws(Exception::class)
    suspend fun dbGetCommandContent(command: String): List<String> {
//    fun dbGetCommandContent(command: String): Flow<List<String>> = flow {
        return withContext(Dispatchers.IO) {
            val content: MutableList<String> = mutableListOf<String>()
//        var content = ""
            val commandSplitted: List<String> = command.split("\\s+".toRegex())
//            val query = if (commandSplitted.size == 1) {
//                "SELECT filename, content FROM file WHERE filename = '$command'"
//            } else {
//                "SELECT filename, content FROM file WHERE id IN (SELECT rowid FROM file_fts WHERE file_fts MATCH 'content:${
//                    formatRegex(
//                        commandSplitted
//                    )
//                }')"
//            }
            val query =
                """SELECT filename, content FROM file WHERE filename = '$command'
                    UNION
                    SELECT filename, content FROM file WHERE id IN (SELECT rowid FROM file_fts WHERE file_fts MATCH 'content:${
                    formatRegex(
                        commandSplitted
                    )
                }') AND NOT EXISTS ( SELECT 1 FROM file WHERE filename = '$command')"""

//            val statement = db.prepare(query)//.use { statement ->
//            val hasStep = statement.step()
//            if (hasStep) {
//                try {
//                    val count = statement.getLong(0)
//                    Log.i("[Database Row Count]", "$count")
//                } catch (error: Exception) {
//                    Log.i("[Database Row Count]", "${error.message}")
//                }
//                content = statement.getText(2)
//                statement.close()
//            } else {
//                throw Exception("Statement.step() => $hasStep")
//            }
//        }
//        if (statement.step()) {
//            val countRow = statement.getLong(0)
//            Log.d("[SQL QUERIES]", query)
//            Log.i("[Database Row Count]", "$countRow")
//        }
//            val res = async {
            db.prepare(query).use { statement ->
                var count = 1
                while (statement.step()) {
//                        val countRow = statement.getLong(0)
//                        Log.i("[Database Row Count]", "$countRow")
//            content.add("File ($count): ${statement.getText(1)}\n")
                    val added = content.add("File ($count): ${statement.getText(0)}")
                    Log.i(this::class.simpleName, "Added ($count)($added): ${statement.getText(0)}")
                    count++
                }
                statement.close()
            }
//            }
//            res.await()
            return@withContext content
        }
//        return content

//        return content

    }

    fun formatRegex(commandSplit: List<String>): String {
        var reg = ""
        for (word in commandSplit) {
            reg += "$word* "
        }
        return reg.trimEnd()
    }
}