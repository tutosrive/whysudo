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