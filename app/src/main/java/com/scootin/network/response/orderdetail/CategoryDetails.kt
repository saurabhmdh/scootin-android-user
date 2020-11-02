package com.scootin.network.response.orderdetail

data class CategoryDetails(
    val active: Boolean,
    val deleted: Boolean,
    val description: String,
    val id: Int,
    val name: String
)