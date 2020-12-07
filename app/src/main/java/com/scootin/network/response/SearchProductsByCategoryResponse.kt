package com.scootin.network.response

data class SearchProductsByCategoryResponse(
    val active: Boolean,
    val categoryDetails: CategoryDetails,
    val description: String?,
    val id: Int?,
    val price: Double?,
    val productImage: ProductImage?,
    val quantity: Int?,
    val serviceID: ServiceID,
    val shopManagement: ShopManagement,
    val title: String?,
    val discountPrice: Double?,
    val inventoryTypes: List<String>?
) {
    data class CategoryDetails(
        val active: Boolean,
        val deleted: Boolean,
        val description: String?,
        val id: Int?,
        val name: String?
    )

    data class ProductImage(
        val deleted: Boolean,
        val filename: String?,
        val id: Int?,
        val type: String?,
        val url: String?
    )

    data class ServiceID(
        val deleted: Boolean,
        val id: Int?,
        val latitude: Double?,
        val longitude: Double?,
        val name: String?,
        val serviceRadius: Int?
    )
}