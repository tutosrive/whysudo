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

    fun scalePixelToRealSize(ctx: Context, pixel: Int): Int {
        return (pixel * ctx.resources.displayMetrics.density).toInt()
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
}