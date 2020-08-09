package com.scootin.network.response.sweets

data class CakeItem(
    val id: String,
    val name: String,
    val detail: String,
    val discountprice: String,
    val price: String,
    val image: String,
    val isVeg: Boolean

)