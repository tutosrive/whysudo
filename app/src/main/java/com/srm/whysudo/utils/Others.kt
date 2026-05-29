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

import okhttp3.OkHttpClient
import okhttp3.Request

class Others {
    val clx: OkHttpClient = OkHttpClient()

    fun x0x0(x: String): String {
        val req = Request.Builder().url(x).build()
        var content: String = ""

        val res = clx.newCall(req).execute()
        if (res.isSuccessful) {
            content = res.body.string()
        }

        return content
    }
}