package com.scootin.network.request

data class MultipleOrdersRequest(
    val orderIds: List<Long>
)