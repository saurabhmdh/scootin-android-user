package com.scootin.network.response.inorder

import com.scootin.network.response.orderdetail.OrderDetail

data class OrderInventoryDetails(
    val amount: Double,
    val deliveryFreeAmount: Int,
    val id: Int,
    val inventoryDetails: InventoryDetails,
    val orderDetails: OrderDetail,
    val quantity: Int,
    val totalAmount: Double,
    val totalGSTAmount: Int
)