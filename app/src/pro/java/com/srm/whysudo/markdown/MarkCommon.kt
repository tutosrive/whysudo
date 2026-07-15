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

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srm.whysudo.MainActivity
import com.srm.whysudo.R
import com.srm.whysudo.interfaces.MarkCommon
import io.noties.markwon.Markwon
import io.noties.markwon.recycler.MarkwonAdapter
import org.commonmark.node.FencedCodeBlock

class MarkCommon : MarkCommon {
    private lateinit var adapterListFencedCode: MarkwonAdapter
    override fun configAdapterMarkwon(ctx: Context): Unit {
        when (ctx) {
            is MainActivity -> {
                adapterListFencedCode = MarkwonAdapter
                    .builderTextViewIsRoot(R.layout.default_mark_entry)
                    .include<FencedCodeBlock>(
                        FencedCodeBlock::class.java,
                        CustomFencedCodeEntry(ctx)
                    ).build()
                val activity = ctx as Activity
                val recyclerView: RecyclerView =
                    activity.findViewById<RecyclerView>(R.id.infoTextMainR)
                recyclerView.layoutManager = LinearLayoutManager(ctx)
                recyclerView.itemAnimator = DefaultItemAnimator()
                recyclerView.adapter = adapterListFencedCode
            }
        }
    }

    fun setMark(markwon: Markwon, txt: String): Unit {
        this.adapterListFencedCode.setMarkdown(markwon, txt)
        this.adapterListFencedCode.notifyDataSetChanged()
    }
}