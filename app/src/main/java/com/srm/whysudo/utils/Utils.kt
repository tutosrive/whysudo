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
import java.io.InputStream

object Utils {
    fun loadAssetFile(ctx: Context, filename: String): InputStream {
        val file = ctx.assets.open(filename)
        return file
    }

    fun readAssetTextFile(ctx: Context, filename: String): String {
        val read = loadAssetFile(ctx, filename).bufferedReader().use { it.readText() }
        return read
    }
}