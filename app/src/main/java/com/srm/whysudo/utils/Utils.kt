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
import android.widget.TextView
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Calendar

object Utils {
    fun loadAssetFile(ctx: Context, filename: String): InputStream {
        val file = ctx.assets.open(filename)
        return file
    }

    fun readAssetTextFile(ctx: Context, filename: String): String {
        val read = loadAssetFile(ctx, filename).bufferedReader().use { it.readText() }
        return read
    }

    fun getNow(): Calendar {
        val now = Calendar.getInstance()
        return now
    }

    fun getNowYear(): Int {
        return getNow().get(Calendar.YEAR)
    }

    fun setFooterContent(footer: TextView, strFooter: String, markman: MarkwonManager) {
        val year: Int = getNowYear()
        var text: String = ""
        if (strFooter.contains("YEAR")) text = strFooter.replace("YEAR", "$year")
        markman.setMark(text, footer)
    }

    fun copyFileFromAssets(ctx: Context, filename: String, isDb: Boolean = false) {
        try {
            val outputFile: File = if (isDb) {
                File("${ctx.dataDir}/databases/", filename)
            } else {
                File(ctx.filesDir, filename)
            }

            if (!outputFile.exists()) {
                val file = loadAssetFile(ctx, filename)
//                outputFile.parentFile?.mkdirs()

                file.use { inputStream ->
                    FileOutputStream(outputFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }
            Log.i("[Log Database exists]", "${outputFile.exists()}: ${outputFile.absolutePath}")
        } catch (e: Exception) {
            Log.e("[ERROR loading asset]", "${e.message}")
        }
    }
}