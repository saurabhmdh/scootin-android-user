package com.scootin.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scootin.network.response.SearchISuggestiontem

object Conversions {
    fun convertExtraData(data: String?):List<SearchISuggestiontem> {
        val listType = object : TypeToken<List<SearchISuggestiontem>>() {}.type
        return  Gson().fromJson<List<SearchISuggestiontem>>(data, listType)
    }
}