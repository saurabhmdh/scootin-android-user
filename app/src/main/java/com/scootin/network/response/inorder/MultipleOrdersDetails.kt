package com.scootin.network.response.inorder

import com.scootin.network.response.orderdetail.OrderDetail

data class MultipleOrdersDetails(
    val orderDetails: List<OrderDetail>,
    val orderInventoryDetailsList: List<OrderInventoryDetails>
)