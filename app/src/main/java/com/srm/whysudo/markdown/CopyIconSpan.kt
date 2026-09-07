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

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.style.LeadingMarginSpan
import io.noties.markwon.utils.LeadingMarginUtils

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
            c.translate(left, top.toFloat() + 10f)
            icon.draw(c)
        } finally {
            c.restoreToCount(save)
        }
    }
}