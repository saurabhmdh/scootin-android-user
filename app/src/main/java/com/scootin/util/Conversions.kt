package com.scootin.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scootin.network.response.ExtraDataItem

object Conversions {
    fun convertExtraData(data: String?):List<ExtraDataItem> {
        val listType = object : TypeToken<List<ExtraDataItem>>() {}.type
        return  Gson().fromJson<List<ExtraDataItem>>(data, listType)
    }
}