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

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.srm.whysudo.R
import io.noties.markwon.Markwon
import io.noties.markwon.recycler.MarkwonAdapter
import org.commonmark.node.FencedCodeBlock

class CustomFencedCodeEntry(val ctx: Context) :
    MarkwonAdapter.Entry<FencedCodeBlock, CustomFencedCodeEntry.Holder>() {
    override fun createHolder(p0: LayoutInflater, p1: ViewGroup): CustomFencedCodeEntry.Holder {
        return Holder(
            p0.inflate(
                R.layout.custom_fenced_code_view,
                p1,
                false
            ),
            ctx
        )
    }

    override fun bindHolder(
        p0: Markwon,
        p1: Holder,
        p2: FencedCodeBlock
    ) {
        p0.setParsedMarkdown(p1.commandText, p0.render(p2))
    }

    class Holder(itemView: View, val ctx: Context) : MarkwonAdapter.Holder(itemView) {
        var commandText: TextView = requireView<TextView>(R.id.command)
        var btnCopy: ImageButton = requireView<ImageButton>(R.id.btnCopyCommand)

        init {
            btnCopy.setOnClickListener { v ->
                copyToClipboard()
            }
        }

        private fun copyToClipboard() {
            val text = commandText.text.trim()
            val clipBoard = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val textClip = ClipData.newPlainText("Command", text)
            clipBoard.setPrimaryClip(textClip)
            Toast.makeText(
                ctx,
                "Command copied to clipboard!",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}