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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class RawDataManager(private val ctx: Context, val fileDataName: String) {
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
