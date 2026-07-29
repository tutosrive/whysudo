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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import com.srm.whysudo.R
import kotlinx.coroutines.withContext

class BtnDialog(val text: String, val callback: (() -> Any)? = null)

class ConfigDialog(
    val msg: String,
    val title: String,
    val buttonAccept: BtnDialog? = null,
    val buttonClose: BtnDialog? = null,
    val callbackOnDismiss: (() -> Unit)? = null,
    var iconDrawableId: Int = R.drawable.ic_info
)

class CustomDialog(ctx: Context, val configDialog: ConfigDialog) {
    var builder = AlertDialog.Builder(ctx)

    init {
        val drawable = AppCompatResources.getDrawable(ctx, configDialog.iconDrawableId)
        builder.setIcon(drawable)
        builder.setTitle(configDialog.title)
        builder.setMessage(configDialog.msg)
        makeButtons()
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun makeButtons() {
        if (configDialog.buttonAccept != null) {
            val text: String = configDialog.buttonAccept.text
            builder.setPositiveButton(text) { dialog, _ ->
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