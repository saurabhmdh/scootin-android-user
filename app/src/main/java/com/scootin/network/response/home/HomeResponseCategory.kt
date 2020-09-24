package com.scootin.network.response.home

data class HomeResponseCategory (
    val name: String,
    val description: String,
    val active: Boolean,
    val deleted: Boolean,
    val id: Int
)