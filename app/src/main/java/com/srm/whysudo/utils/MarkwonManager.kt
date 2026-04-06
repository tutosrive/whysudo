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
import com.srm.whysudo.R
import android.widget.TextView
import com.srm.whysudo.MainActivity
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.core.MarkwonTheme
import org.commonmark.node.Code
import org.commonmark.node.ListBlock
import org.commonmark.node.Node

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
        return Markwon.builder(ctx).usePlugin(object : AbstractMarkwonPlugin() {
            override fun configureTheme(builder: MarkwonTheme.Builder) {
                super.configureTheme(builder)
                when (ctx) {
                    is MainActivity -> {
                        codeStyles(builder)
                        listStyles(builder)
                    }
                }
            }

            override fun configureVisitor(builder: MarkwonVisitor.Builder) {
                super.configureVisitor(builder)
                builder.on(Code::class.java) { visitor, node ->
                    val length: Int = visitor.length()
                    val code: Node = node.parent
                    val previousIsList: Boolean = code.previous is ListBlock
                    val whiteSpace: String = " ".repeat(node.literal.length)
                    val padding: String = "  "

                    if (previousIsList) {
                        visitor.builder().append("$padding$whiteSpace$padding\n$padding")
                        visitor.builder().append(node.literal)
                        visitor.builder().append("$padding\n$padding$whiteSpace$padding\n")
                    } else {
                        visitor.builder().append(padding)
                        visitor.builder().append(node.literal)
                        visitor.builder().append(padding)
                    }

                    visitor.setSpansForNodeOptional(node, length)
                }
            }
        })
    }


    private fun codeStyles(b: MarkwonTheme.Builder) {
        val textSize: Int = Utils.scalePixelToRealSize(ctx, 14)
        val codeBlockMargin: Int = Utils.scalePixelToRealSize(ctx, 5)
        val blockTextSize: Int = Utils.scalePixelToRealSize(ctx, 10)
        val bgColor: Int = ctx.getColor(R.color.dark_blue)
        val textColor: Int = ctx.getColor(R.color.code_color)

        b
            .codeTextSize(textSize)
            .codeTextColor(textColor)
            .codeBackgroundColor(bgColor)
            .codeBlockMargin(codeBlockMargin)
            .codeBlockTextSize(blockTextSize)
    }

    private fun listStyles(b: MarkwonTheme.Builder) {
        val bulletWidth: Int = Utils.scalePixelToRealSize(ctx, 4)
        b.bulletWidth(bulletWidth)
    }
}