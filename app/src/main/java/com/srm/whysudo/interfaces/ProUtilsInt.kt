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

package com.srm.whysudo.interfaces

import android.content.Context
import android.widget.ImageView
import com.srm.whysudo.models.CommandModelView
import com.srm.whysudo.utils.DBDataManager
import kotlinx.coroutines.Job

interface ProUtilsInt {
    fun toggleFavoriteIcon(imageViewer: ImageView, isFavorite: Boolean): Unit
    fun setCommandAsFavorite(
        ctx: Context, db: DBDataManager, command: CommandModelView, view: ImageView
    ): Job

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