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

package com.srm.whysudo.interfaces

import android.content.Context
import android.widget.ImageView
import com.srm.whysudo.adapters.CommandModelView
import com.srm.whysudo.utils.DBDataManager

interface ProUtilsInt {
    fun toggleFavoriteIcon(imageViewer: ImageView, isFavorite: Boolean): Unit
    fun setCommandAsFavorite(
        ctx: Context, db: DBDataManager, command: CommandModelView, view: ImageView
    ): Unit

    fun showFavoriteSetToast(ctx: Context, isOk: Boolean): Unit
    fun showToast(ctx: Context, msg: String): Unit
    fun showToast(ctx: Context, resId: Int): Unit
    fun getFavoriteIconId(isFavorite: Boolean): Int
    fun notifyFavoriteToView(
        imageViewer: ImageView,
        isFavorite: Boolean,
        animate: Boolean = true,
        duration: Long = 300L
    ): Unit

}