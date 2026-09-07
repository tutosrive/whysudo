/*
 * Copyright (c) 2026 tutosrive
 *
 * Author: tutosrive
 * GitHub: https://github.com/tutosrive
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.srm.whysudo.markdown

import android.content.Context
import android.graphics.Typeface
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import com.srm.whysudo.MainActivity
import com.srm.whysudo.R
import com.srm.whysudo.utils.Utils
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.movement.MovementMethodPlugin

class MarkdownManager(val ctx: Context) {
    var mark: Markwon
    val markCommon: MarkCommon = MarkCommon()

    init {
        val markBuilder = configMarkwon()
        mark = markBuilder.build()
        // TODO: Create this common class in FREE
        markCommon.configAdapterMarkwon(ctx)
    }

    fun setMark(text: String) {
        markCommon.setMark(mark, text)
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
                        }
                    }
                    listStyles(builder)
                }
            })
            .usePlugin(MovementMethodPlugin.create(ScrollingMovementMethod.getInstance()))
    }

    private fun codeStyles(b: MarkwonTheme.Builder) {
        val blockTextSize: Int = Utils.scalePixelToRealSize(ctx, 12)
        val bgColor: Int = ctx.getColor(R.color.dark_blue)
        val textColor: Int = ctx.getColor(R.color.code_block_color)
        val typefaceCode: Typeface = Typeface.create(Typeface.MONOSPACE, Typeface.ITALIC)
        val typefaceCodeBlock: Typeface = Typeface.MONOSPACE

        b
            .codeTypeface(typefaceCode)
            .codeBlockTypeface(typefaceCodeBlock)
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
}