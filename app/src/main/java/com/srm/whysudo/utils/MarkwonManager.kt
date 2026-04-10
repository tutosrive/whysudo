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
import android.graphics.Typeface
import android.widget.TextView
import android.widget.Toast
import com.srm.whysudo.MainActivity
import com.srm.whysudo.R
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme

class MarkwonManager(val ctx: Context) {
    var mark: Markwon

    init {

        val markBuilder = configMarkwon()
        mark = markBuilder.build()
    }

    fun setMark(text: String, textView: TextView) {
        mark.setMarkdown(textView, text)
    }

    private fun configMarkwon(): Markwon.Builder {
        return Markwon.builder(ctx)
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureTheme(builder: MarkwonTheme.Builder) {
                    when (ctx) {
                        is MainActivity -> {
                            codeStyles(builder)
                            listStyles(builder)
                        }
                    }
                }
            })
    }

    private fun codeStyles(b: MarkwonTheme.Builder) {
        val textSize: Int = Utils.scalePixelToRealSize(ctx, 14)
        val codeBlockMargin: Int = Utils.scalePixelToRealSize(ctx, 5)
        val blockTextSize: Int = Utils.scalePixelToRealSize(ctx, 12)
        val bgColor: Int = ctx.getColor(R.color.dark_blue)
        val textColor: Int = ctx.getColor(R.color.text_color_on_dark)

        b
            .codeTextColor(textColor)
            .codeBackgroundColor(bgColor)
            .codeBlockTextColor(textColor)
            .codeBlockBackgroundColor(bgColor)
            .codeBlockTextSize(blockTextSize)
            .codeBlockTypeface(Typeface.MONOSPACE)
    }

    private fun listStyles(b: MarkwonTheme.Builder) {
        val bulletWidth: Int = Utils.scalePixelToRealSize(ctx, 4)
        b.bulletWidth(bulletWidth)
    }

    fun copyToClipboard(content: String): Unit {
        Utils.copyToClipboard(ctx, "Command", content)
        Toast.makeText(
            ctx,
            R.string.copy_clipboard_successfully,
            Toast.LENGTH_LONG
        ).show()
    }
}