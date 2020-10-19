package com.scootin.network.response

import androidx.annotation.Keep


@Keep
data class SearchShopsByCategoryResponse(
    val distance: String,
    val imageUrl: String,
    val name: String,
    val online: Boolean,
    val rating: Double,
    val shopID: Int
)