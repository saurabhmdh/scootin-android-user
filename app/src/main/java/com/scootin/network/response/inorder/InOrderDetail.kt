package com.scootin.network.response.inorder

import com.scootin.network.response.orderdetail.OrderDetail

data class InOrderDetail(
    val orderDetails: OrderDetail,
    val orderInventoryDetailsList: List<OrderInventoryDetails>
)