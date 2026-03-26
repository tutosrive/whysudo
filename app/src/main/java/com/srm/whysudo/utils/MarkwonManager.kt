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
import android.widget.TextView
import io.noties.markwon.Markwon

class MarkwonManager(ctx: Context) {
    val mark: Markwon = Markwon.create(ctx)

    init {
        val conf = mark.configuration()
    }

    fun setMark(text: String, textView: TextView) {
        mark.setMarkdown(textView, text)
    }
}