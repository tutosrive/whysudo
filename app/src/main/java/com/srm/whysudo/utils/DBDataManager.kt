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
import android.database.Cursor
import android.util.Log
import com.srm.whysudo.adapters.CommandModelView
import com.srm.whysudo.database_man.DbManager
import com.srm.whysudo.database_man.DbStarter
import com.srm.whysudo.enums.DataFileName
import com.srm.whysudo.examples.MigrationStub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.zetetic.database.sqlcipher.SQLiteDatabase

class DBDataManager(
    private val ctx: Context,
    val callbackOnStartLoad: (() -> Unit)? = null,
    val callbackOnFinishLoad: (() -> Unit)? = null,
    val callbackOnError: (() -> Unit)? = null
) {
    val sharedPref = SharedSettings(this.ctx)
    val fileDataName: String = DataFileName.DB_COMMANDS()
    private lateinit var dbMan: DbManager
    private lateinit var db: SQLiteDatabase
    private val qr: String = loadQr()

    init {
        if (qr.length == 162) {
            loadData()
        } else {
            callbackOnError?.invoke()
        }
    }


    private fun loadData() {
        try {
            val job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    callbackOnStartLoad?.invoke()
                    Utils.copyFileFromAssets(ctx = ctx, filename = fileDataName, isDb = true)
                } catch (e: Exception) {
                    throw e
                }
            }

            job.invokeOnCompletion {
                try {
                    val job2 = CoroutineScope(Dispatchers.IO).launch {
                        dbMan = DbManager(fileDataName, ctx, qr)
                    }
                    job2.invokeOnCompletion {
                        db = dbMan.conn
                        callbackOnFinishLoad?.invoke()
                    }
                } catch (e: Exception) {
                    throw e
                }
            }

        } catch (e: Exception) {
            callbackOnError?.invoke()
        }
    }

    @Throws(Exception::class)
    suspend fun getFileNames(
        command: String,
        limit: Int? = 40,
        lang: String = "en"
    ): List<CommandModelView> {
        return withContext(Dispatchers.IO) {
            val fileNames: MutableList<CommandModelView> = mutableListOf<CommandModelView>()
            val cmdSplit: List<String> = command.split("\\s+".toRegex())
            val cmd = cmdSplit.joinToString(separator = "-")
            val query =
                """SELECT F.id, filename, is_pro, is_favorite FROM file F
                    INNER JOIN version V ON F.type_version = V.id
                    WHERE filename = ?
                    OR F.id IN (SELECT rowid FROM file_fts WHERE file_fts MATCH '$lang:${
                    formatRegex(cmdSplit)
                }') AND NOT EXISTS ( SELECT 1 FROM file WHERE filename = ?)
                LIMIT $limit"""
            db.rawQuery(query, arrayOf(cmd)).use {
                while (it.moveToNext()) {
                    val commandObj = makeCommandObj(it)
                    fileNames.add(commandObj)
                }
                it.close()
            }
            sortData(fileNames, command)
            return@withContext fileNames
        }
    }

    private fun makeCommandObj(res: Cursor): CommandModelView {
        val id = res.getInt(0)
        val name = res.getString(1)
        val type = Utils.intToBoolean(
            res.getInt(2)
        )
        val favorite = Utils.intToBoolean(
            res.getInt(3)
        )
        val obj: CommandModelView = CommandModelView(
            id, name,
            type, favorite
        )
        return obj
    }

    suspend fun sortData(data: MutableList<CommandModelView>, command: String) {
        withContext(Dispatchers.IO) {
            val query = command.lowercase().trim()

            data.sortWith(
                compareByDescending<CommandModelView> { item ->
                    val cmd = item.filename.lowercase()
                    when {
                        cmd == query -> 1000
                        cmd.startsWith(query) -> 500
                        cmd.startsWith("$query-") -> 400
                        cmd.contains(query) -> 100

                        else -> 0
                    }
                }.thenBy { it.filename.length }
                    .thenBy { it.filename }
            )
        }
    }

    @Throws(Exception::class)
    suspend fun getCommandContent(command: String, lang: String = "en"): String {
        return withContext(Dispatchers.IO) {
            var content: String
            val query = """SELECT $lang FROM content_language C
                inner join file F on C.id = F.id_data WHERE filename = ?"""
            db.rawQuery(query, arrayOf(command)).use {
                it.moveToFirst()
                content = it.getString(0)
                it.close()
            }
            return@withContext content
        }
    }

    suspend fun getAllCommands(): List<CommandModelView> {
        return withContext(Dispatchers.IO) {
            val filenames: MutableList<CommandModelView> = mutableListOf()
            val query = """SELECT F.id, filename, is_pro, is_favorite FROM file F
                    INNER JOIN version V ON F.type_version = V.id"""
            db.rawQuery(query).use {
                while (it.moveToNext()) {
                    val command: CommandModelView = makeCommandObj(it)
                    filenames.add(command)
                }
                it.close()
            }
            return@withContext filenames
        }
    }

    suspend fun getCommandById(id: Int, lang: String = "en"): String {
        return withContext(Dispatchers.IO) {
            val query = """SELECT $lang FROM content_language C
                inner join file F on C.id = F.id_data WHERE C.id = ?"""
            var content = ""
            db.rawQuery(query, arrayOf(id.toString())).use {
                it.moveToFirst()
                content = it.getString(0)
            }

            return@withContext content
        }
    }

    suspend fun saveFavorite(id: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val queryUpdate: String = "UPDATE file SET is_favorite = 1 WHERE id = ?"
            val queryGet: String = "SELECT is_favorite FROM file WHERE id = ?"
            var isOK = false

            try {
                db.execSQL(queryUpdate, arrayOf(id.toString()))
                db.rawQuery(queryGet, arrayOf(id.toString())).use {
                    val cols = it.columnNames.joinToString(",")
                    Log.i("saveToFavorite", cols)
                    it.moveToFirst()
                    if (it.getInt(0) > 0) {
                        isOK = true
                    }
                }
            } catch (e: Exception) {
                Log.e("saveFavorite", e.message.toString())
            }

            return@withContext isOK
        }
    }

    fun formatRegex(commandSplit: List<String>): String {
        var reg = ""
        for (word in commandSplit) reg += "$word* "
        return reg.trimEnd()
    }

    private fun loadQr(): String {
        var res = sharedPref.getPref("r", "none")
        if (res == "none" || res.isNullOrEmpty() || res.isBlank()) {
            val a = MigrationStub.axk()
            res = a
            sharedPref.savePref("r", res)
        }
        return res

    }

    fun close() {
        dbMan.close()
    }
}