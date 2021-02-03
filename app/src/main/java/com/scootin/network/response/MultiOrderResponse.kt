package com.scootin.network.response

import com.scootin.network.response.orderdetail.OrderDetail


data class MultiOrderResponse(
    val orderLists : List<OrderDetail>,
    val paymentDetails: PaymentDetails?
)
