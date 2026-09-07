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