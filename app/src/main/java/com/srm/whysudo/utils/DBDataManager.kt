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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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

    @Throws(Exception::class)
    suspend fun getFileNames(command: String): List<String> {
        return withContext(Dispatchers.IO) {
            val fileNames: MutableList<String> = mutableListOf<String>()
            val cmdSplit: List<String> = command.split("\\s+".toRegex())
            val cmd = cmdSplit.joinToString(separator = "-")
            val query =
                """SELECT filename FROM file WHERE filename = '$cmd'
                    OR id IN (SELECT rowid FROM file_fts WHERE file_fts MATCH 'content:${
                    formatRegex(cmdSplit)
                }') AND NOT EXISTS ( SELECT 1 FROM file WHERE filename = '$cmd')
                LIMIT 20"""
            db.prepare(query).use { statement ->
                while (statement.step()) {
                    fileNames.add(statement.getText(0))
                }
                statement.close()
            }
            Log.i(this::class.simpleName, fileNames.toString())
            return@withContext fileNames
        }
    }

    @Throws(Exception::class)
    suspend fun getCommandContent(command: String): String {
        return withContext(Dispatchers.IO) {
            var content: String
            val query = "SELECT content FROM file WHERE filename = '$command'"
            db.prepare(query).use { statement ->
                statement.step()
                content = statement.getText(0)
                statement.close()
            }
            return@withContext content
        }
    }

    fun formatRegex(commandSplit: List<String>): String {
        var reg = ""
        for (word in commandSplit) reg += "$word* "
        return reg.trimEnd()
    }
}