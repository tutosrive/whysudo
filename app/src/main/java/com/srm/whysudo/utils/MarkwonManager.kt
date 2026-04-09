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
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.Spanned
import android.text.TextPaint
import android.text.method.ScrollingMovementMethod
import android.text.style.ClickableSpan
import android.text.style.LeadingMarginSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.srm.whysudo.MainActivity
import com.srm.whysudo.R
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.movement.MovementMethodPlugin
import io.noties.markwon.utils.LeadingMarginUtils
import org.commonmark.node.FencedCodeBlock
import kotlin.jvm.java

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
                    super.configureTheme(builder)
                    when (ctx) {
                        is MainActivity -> {
                            codeStyles(builder)
                            listStyles(builder)
                        }
                    }
                }

                override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
                    builder.appendFactory(FencedCodeBlock::class.java) { _, _ ->
                        CopyContentsSpan { copyToClipboard(it) }
                    }

                    builder.appendFactory(FencedCodeBlock::class.java) { _, _ ->
                        CopyIconSpan(AppCompatResources.getDrawable(ctx, R.drawable.shell_text)!!)
                    }
                }
            })
            .usePlugin(MovementMethodPlugin.create(ScrollingMovementMethod.getInstance()))
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

    class CopyContentsSpan(val callback: (c: String) -> Unit) : ClickableSpan() {
        override fun onClick(widget: View) {
            val spanned = (widget as? TextView)?.text as? Spanned ?: return
            val start = spanned.getSpanStart(this)
            val end = spanned.getSpanEnd(this)
            // by default code blocks have new lines before and after content
            val contents = spanned.subSequence(start, end).toString().trim()
            // copy code here
            callback(contents)
        }

        override fun updateDrawState(ds: TextPaint) {
            // do not apply link styling!
        }
    }

    class CopyIconSpan(val icon: Drawable) : LeadingMarginSpan {

        init {
            if (icon.bounds.isEmpty) {
                icon.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
            }
        }

        override fun getLeadingMargin(first: Boolean): Int = 0

        override fun drawLeadingMargin(
            c: Canvas,
            p: Paint,
            x: Int,
            dir: Int,
            top: Int,
            baseline: Int,
            bottom: Int,
            text: CharSequence,
            start: Int,
            end: Int,
            first: Boolean,
            layout: Layout
        ) {

            // called for each line of text, we are interested only in first one
            if (!LeadingMarginUtils.selfStart(start, text, this)) return

            val save = c.save()
            try {
                // horizontal position for icon
                val w = icon.bounds.width().toFloat()
                // minus quarter width as padding
                val left = layout.width - w - (w / 4F)
                c.translate(left, top.toFloat() + 8f)
                icon.draw(c)
            } finally {
                c.restoreToCount(save)
            }
        }
    }
}