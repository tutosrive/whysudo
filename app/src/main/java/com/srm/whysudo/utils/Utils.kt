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

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import com.srm.whysudo.markdown.MarkdownManager
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Calendar
import kotlin.random.Random
import com.srm.whysudo.enums.DataFileName as dfn

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

    fun setFooterContent(footer: TextView, strFooter: String, markman: MarkdownManager) {
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

                file.use { inputStream ->
                    FileOutputStream(outputFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }
        } catch (e: Exception) {
        }
    }

    fun loadLicensesFiles(ctx: Context) {
        val filenames = listOf(
            dfn.LICENSE_MARKWON(),
            dfn.LICENSE_SQLCIPHER(),
            dfn.LICENSE_ANDROIDX()
        )

        filenames.forEach {
            copyFileFromAssets(ctx, it)
        }
    }

    fun readInternalFile(ctx: Context, filename: String): String {
        var content = ""
        try {
            val file = File("${ctx.filesDir}/$filename")
            file.bufferedReader().use {
                content = it.readText()
            }
        } catch (e: Exception) {
        }
        return content
    }

    fun scalePixelToRealSize(ctx: Context, pixel: Int): Int {
        return (pixel * ctx.resources.displayMetrics.density).toInt()
    }

    fun blackAnd(): String {
        val c = "tutosrive"
        return x(c)
    }

    fun copyToClipboard(ctx: Context, msg: String, content: String): Unit {
        val clipboard = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val data = ClipData.newPlainText(msg, content)
        clipboard.setPrimaryClip(data)
    }

    fun goToAnActivity(ctx: Context, v: View, c: Class<*>): Unit {
        val intentActivity = Intent(ctx, c)
        ctx.startActivity(intentActivity)
    }

    fun setViewVisibility(view: View, value: Int): Unit {
        val viewVisibility = view.visibility

        if (viewVisibility != value) {
            view.visibility = value
        }
    }

    fun x(y: String, z: Int? = null): String {
        if (z != null && z in 1..2) {
            return listOf<String>("user", "content")[if (z == 1) 0 else 1]
        } else {
            if (z != null) {
                if (z > 2) {
                    return listOf<String>("638f54737aebeba59256b5abc62ac99a")[0]
                }
            }
        }
        return y
    }

    fun getRandomIdInt(): Int {
        return Random.nextInt(1, 1826)
    }

    fun snakeCaseToCapital(text: String): String {
        val initVal = text.replace("_", " ")
        val res = initVal.split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }

        return res
    }

    fun b(): String {
        return "gist"
    }
}