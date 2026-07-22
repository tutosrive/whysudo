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

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.srm.whysudo.R
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
                val dbDir = File(ctx.dataDir, "databases")
                dbDir.mkdirs()
                File(dbDir, filename)
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
            throw e
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

    fun goToAnActivity(ctx: Context, c: Class<*>): Unit {
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
            return listOf<String>("i", "5")[if (z == 1) 0 else 1]
        } else {
            if (z != null) {
                if (z > 2) {
                    return listOf<String>("2")[0]
                }
            }
        }
        return y
    }

    fun getRandomIndexFree(): Int {
        val indexes: List<Int> = listOf(
            32, 35, 37, 40, 42, 43, 48, 49, 50, 51, 76, 79, 80, 81, 83, 85, 86, 91, 96,
            102, 103, 104, 109, 111, 114, 123, 125, 126, 146, 147, 163, 165, 167, 171, 175,
            179, 182, 185, 201, 203, 204, 206, 217, 221, 223, 225, 228, 230, 233, 238, 240,
            242, 243, 251, 255, 256, 257, 260, 269, 270, 271, 275, 276, 277, 284, 289, 293,
            295, 299, 302, 303, 312, 314, 318, 331, 333, 334, 335, 338, 348, 349, 351, 352,
            353, 367, 376, 377, 378, 385, 386, 391, 393, 398, 400, 409, 415, 418, 421, 433,
            436, 437, 444, 446, 458, 467, 471, 475, 476, 477, 479, 487, 488, 489, 500, 502,
            504, 506, 509, 515, 516, 517, 518, 521, 525, 526, 527, 528, 529, 530, 531, 532,
            535, 542, 658, 659, 660, 665, 666, 675, 676, 681, 685, 687, 689, 693, 694, 695,
            700, 705, 706, 708, 714, 725, 729, 730, 742, 743, 745, 749, 750, 751, 767, 770,
            772, 773, 775, 778, 787, 788, 789, 793, 794, 810, 807, 850, 851, 882, 883, 885,
            886, 887, 888, 890, 892, 893, 895, 900, 901, 902, 903, 906, 907, 908, 909, 910,
            911, 912, 913, 914, 915
        )
        return indexes.random()
    }

    fun intToBoolean(int: Int): Boolean {
        val bool = int != 0
        return bool
    }

    fun getRandomIdInt(): Int {
        return Random.nextInt(1, 1993)
    }

    fun snakeCaseToCapital(text: String): String {
        val initVal = text.replace("_", " ")
        val res = initVal.split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }

        return res
    }

    fun getBadgeProFreeIconId(isPro: Boolean): Int {
        return when (isPro) {
            true -> R.drawable.prob
            false -> R.drawable.freeb
        }
    }

    fun b(): String {
        return "gist"
    }

    fun Activity.hideKeyboard() {
        WindowInsetsControllerCompat(window, window.decorView).hide(
            WindowInsetsCompat.Type.ime()
        )
    }
}