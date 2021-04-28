package com.scootin.network.request

data class RequestSearchWithCategoryAndSubCategory(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val query: String = "",
    val categoryId: Long = -1,
    val subCategoryId: Long = -1
)