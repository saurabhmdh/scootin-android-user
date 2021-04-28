package com.scootin.network.request

data class RequestSearchWithCategoryAndSubCategory(
    val query: String = "",
    val categoryId: Long = -1,
    val subCategoryId: Long = -1
)