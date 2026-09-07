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