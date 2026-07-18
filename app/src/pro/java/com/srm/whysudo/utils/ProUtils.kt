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

import android.widget.ImageView
import com.srm.whysudo.R

object ProUtils {
    fun notifyFavoriteToView(imageViewer: ImageView, isFavorite: Boolean): Unit {
        when (isFavorite) {
            true -> imageViewer.setImageResource(R.drawable.ic_star_filled)
            false -> imageViewer.setImageResource(R.drawable.ic_star)
        }
    }
}