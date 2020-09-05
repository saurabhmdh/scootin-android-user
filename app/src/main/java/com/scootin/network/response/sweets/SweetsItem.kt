package com.scootin.network.response.sweets

data class SweetsItem(
    val id: String,
    val name: String,
    val detail: String,
    val discountprice: String,
    val price: String,
    val image: String

)
data class SweetsStore(
    val id: String,
    val name: String,
    val distance: String,
    val rating: Float,
    val isOpen: Boolean,
    val image:String
)