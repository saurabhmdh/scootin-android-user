package com.scootin.network.response.home

import com.scootin.network.response.Media

data class DealResponse(
    val media: Media,
    val dealType: String,
    val id: Long
)