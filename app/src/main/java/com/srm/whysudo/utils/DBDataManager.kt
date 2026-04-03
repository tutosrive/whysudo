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
import android.widget.Toast
import com.srm.whysudo.database_man.DbManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.zetetic.database.sqlcipher.SQLiteDatabase

class DBDataManager(
    private val ctx: Context,
    val callbackOnStartLoad: () -> Unit,
    val callbackOnFinishLoad: () -> Unit
) {
    val fileDataName: String = "data-linux.enc.db"
    private lateinit var dbMan: DbManager
    private lateinit var db: SQLiteDatabase

    init {
        loadData()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadData() {
        try {
            val job = GlobalScope.launch {
                callbackOnStartLoad()
                Utils.copyFileFromAssets(ctx = ctx, filename = fileDataName, isDb = true)
            }

            job.invokeOnCompletion {
                dbMan = DbManager(fileDataName, ctx)
                db = dbMan.conn
                callbackOnFinishLoad()
            }
        } catch (error: Exception) {
            Toast.makeText(ctx, "Error LoadData => ${error.message}", Toast.LENGTH_LONG * 3).show()
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
                LIMIT 40"""
            db.rawQuery(query).use {
                while (it.moveToNext()) {
                    fileNames.add(it.getString(0))
                }
                it.close()
            }
            sortData(fileNames, command)
            return@withContext fileNames
        }
    }

    suspend fun sortData(data: MutableList<String>, command: String) {
        withContext(Dispatchers.IO) {
            val query = command.lowercase().trim()
            if (query.isEmpty()) data.sorted()

            data.sortWith(
                compareByDescending<String> { item ->
                    val cmd = item.lowercase()
                    when {
                        cmd == query -> 1000
                        cmd.startsWith(query) -> 500
                        cmd.startsWith("$query-") -> 400
                        cmd.contains(query) -> 100

                        else -> 0
                    }
                }.thenBy { it.length }
                    .thenBy { it }
            )
        }
    }

    @Throws(Exception::class)
    suspend fun getCommandContent(command: String): String {
        return withContext(Dispatchers.IO) {
            var content: String
            val query = "SELECT content FROM file WHERE filename = '$command'"
            db.rawQuery(query).use {
                it.moveToFirst()
                content = it.getString(0)
                it.close()
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