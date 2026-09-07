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
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.Toast
import com.srm.whysudo.R
import com.srm.whysudo.models.CommandModelView
import com.srm.whysudo.interfaces.ProUtilsInt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object ProUtils : ProUtilsInt {
    override fun toggleFavoriteIcon(imageViewer: ImageView, isFavorite: Boolean): Unit {
        val resId: Int = getFavoriteIconId(isFavorite)
        imageViewer.setImageResource(resId)
    }

    override fun setCommandAsFavorite(
        ctx: Context,
        db: DBDataManager,
        command: CommandModelView,
        view: ImageView
    ): Job {
        return CoroutineScope(Dispatchers.Main).launch {
            val id: Int = command.id
            command.isFavorite = db.saveFavorite(id, 1)
            if (command.isFavorite) {
                notifyFavoriteToView(view, true)
            } else {
                showFavoriteSetToast(ctx, false)
            }
        }
    }

    fun unsetCommandAsFavorite(
        ctx: Context,
        db: DBDataManager,
        command: CommandModelView,
        view: ImageView
    ): Job {
        return CoroutineScope(Dispatchers.Main).launch {
            val id: Int = command.id
            command.isFavorite = db.saveFavorite(id, 0)
            if (!command.isFavorite) {
                notifyFavoriteToView(view, false)
            } else {
                showFavoriteSetToast(ctx, false)
            }
        }
    }

    fun showDialogRemoveFavoriteConfirm(ctx: Context, callbackAccept: () -> Any): Unit {
        val msg: String = ctx.getString(R.string.msg_dialog_remove_favorite)
        val title: String = ctx.getString(R.string.title_dialog_remove_favorite)
        val btnAcceptLabel: String = ctx.getString(R.string.label_btn_accept_remove)
        val btnCloseLabel: String = ctx.getString(R.string.label_btn_close)
        val btnAccept: BtnDialog = BtnDialog(btnAcceptLabel, callback = callbackAccept)
        val btnClose: BtnDialog = BtnDialog(btnCloseLabel)
        val configDialog: ConfigDialog = ConfigDialog(
            msg = msg,
            title = title,
            buttonAccept = btnAccept,
            buttonClose = btnClose,
            iconDrawableId = R.drawable.ic_star_filled_with_x
        )
        CustomDialog(ctx, configDialog)
    }

    override fun showFavoriteSetToast(ctx: Context, isOk: Boolean): Unit {
        val resId: Int = when (isOk) {
            true -> R.string.msg_favorite_set_ok
            false -> R.string.msg_favorite_error
        }
        showToast(ctx, resId)

    }

    override fun showToast(ctx: Context, msg: String): Unit {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showToast(ctx: Context, resId: Int): Unit {
        Toast.makeText(ctx, resId, Toast.LENGTH_SHORT).show()
    }

    override fun getFavoriteIconId(isFavorite: Boolean): Int {
        return when (isFavorite) {
            true -> R.drawable.ic_star_filled
            false -> R.drawable.ic_star
        }
    }

    override fun notifyFavoriteToView(
        imageViewer: ImageView,
        isFavorite: Boolean,
        animate: Boolean,
        duration: Long
    ): Unit {
        if (isFavorite && animate) {
            imageViewer.animate()
                .scaleX(0.5f)
                .scaleY(0.5f)
                .setDuration(duration)
                .withEndAction {
                    toggleFavoriteIcon(imageViewer, isFavorite)
                    imageViewer.animate()
                        .scaleY(1f)
                        .scaleX(1f)
                        .setDuration(duration)
                        .setInterpolator(OvershootInterpolator(3.0f))
                        .start()
                }
                .start()
        } else {
            toggleFavoriteIcon(imageViewer, isFavorite)
        }
    }
}