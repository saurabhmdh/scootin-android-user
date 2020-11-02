package com.scootin.network.response.inorder

data class CategoryDetails(
    val active: Boolean,
    val deleted: Boolean,
    val description: String,
    val id: Int,
    val name: String
)