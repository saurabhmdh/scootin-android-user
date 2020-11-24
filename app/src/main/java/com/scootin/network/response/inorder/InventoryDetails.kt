package com.scootin.network.response.inorder

import com.scootin.network.response.ShopManagement

data class InventoryDetails(
    val active: Boolean,
    val categoryDetails: CategoryDetails,
    val description: String,
    val id: Int,
    val price: Double,
    val productImage: Any,
    val quantity: Int,
    val serviceID: ServiceID,
    val shopManagement: ShopManagement,
    val taxedGST: Any,
    val title: String
)