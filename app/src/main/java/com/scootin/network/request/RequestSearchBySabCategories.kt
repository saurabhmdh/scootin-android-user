package com.scootin.network.request

data class RequestSearchBySabCategories(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val query: String = "",
    val subcategories: List<Long> = mutableListOf()
)
