package com.scootin.network.response.inorder

data class GstInfoReference(
    val deleted: Boolean,
    val filename: String,
    val id: Int,
    val thumb: Any,
    val type: String,
    val url: String
)