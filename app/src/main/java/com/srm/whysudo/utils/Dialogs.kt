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
import android.text.Spanned
import androidx.appcompat.app.AlertDialog
import com.srm.whysudo.markdown.MarkdownManager

class BtnDialog(val text: String, val callback: (() -> Unit)?)

class ConfigDialog(
    val msg: String,
    val title: String,
    val buttonAccept: BtnDialog? = null,
    val buttonClose: BtnDialog? = null,
    val callbackOnDismiss: (() -> Unit)? = null
)

class CustomDialog(ctx: Context, val configDialog: ConfigDialog) {
    var builder = AlertDialog.Builder(ctx)

    init {
        builder.setTitle(configDialog.title)
        val msg: Spanned = MarkdownManager(ctx).mark.toMarkdown(configDialog.msg)
        builder.setMessage(msg)

        makeButtons()

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun makeButtons() {
        if (configDialog.buttonAccept != null) {
            val text: String = configDialog.buttonAccept.text
            builder.setNegativeButton(text) { dialog, _ ->
                if (configDialog.buttonAccept.callback == null) {
                    dialog.dismiss()
                } else {
                    configDialog.buttonAccept.callback()
                }
            }
        }

        if (configDialog.buttonClose != null) {
            val text = configDialog.buttonClose.text
            builder.setNegativeButton(text) { dialog, _ ->
                if (configDialog.buttonClose.callback == null) {
                    dialog.dismiss()
                } else {
                    configDialog.buttonClose.callback()
                }
            }
        }

        builder.setOnDismissListener {
            configDialog.callbackOnDismiss?.invoke()
        }
    }
}