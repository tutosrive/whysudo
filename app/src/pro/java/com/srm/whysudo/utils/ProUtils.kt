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