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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class DataManager(private val ctx: Context, val fileDataName: String) {
    private lateinit var stringData: String
    private lateinit var data: JSONObject

    init {
        loadData()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadData() {
        stringData = Utils.readAssetTextFile(ctx, fileDataName)
        GlobalScope.launch {
            data = JSONObject(stringData)
        }
    }

    fun getData(): JSONObject {
        return data
    }

    fun getJsonByKey(key: String): JSONObject {
        return data.getJSONObject(key)
    }

    fun getStringByKey(key: String): String {
        return data.getString(key)
    }

    fun getArrayByKey(key: String): JSONArray {
        return data.getJSONArray(key)
    }

    fun getContentString(json: JSONObject, key: String = "content"): String {
        return json.getString(key)
    }
}