package com.scootin.network.response

class SearchProductsByCategoryResponse(
    val active: Boolean,
    val categoryDetails: CategoryDetails,
    val description: String?,
    val id: Int?,
    val price: Double?,
    val productImage: ProductImage?,
    val quantity: Int?,
    val serviceID: ServiceID,
    val shopManagement: ShopManagement,
    val title: String?
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

    data class ShopManagement(
        val address: Address,
        val amount: Int?,
        val categoryDetails: CategoryDetails,
        val closeTime: String?,
        val createdAt: Long,
        val deleted: Boolean,
        val discountType: String?,
        val id: Int?,
        val latitude: Double?,
        val longitude: Double?,
        val modified: Long,
        val name: String?,
        val openTime: String?,
        val serviceID: ServiceID,
        val shopBannerReference: ShopBannerReference,
        val shopOwner: ShopOwner,
        val status: Boolean
    ) {
        data class Address(
            val addressLine1: String?,
            val addressLine2: String?,
            val address_type: String?,
            val city: String?,
            val id: Int?,
            val pincode: String?,
            val stateDetails: StateDetails
        ) {
            data class StateDetails(
                val countryDetails: CountryDetails,
                val id: Int?,
                val name: String?
            ) {
                data class CountryDetails(
                    val id: Int?,
                    val name: String?
                )
            }
        }

        data class CategoryDetails(
            val active: Boolean,
            val deleted: Boolean,
            val description: String?,
            val id: Int?,
            val name: String?
        )

        data class ServiceID(
            val deleted: Boolean,
            val id: Int?,
            val latitude: Double?,
            val longitude: Double?,
            val name: String?,
            val serviceRadius: Int?
        )

        data class ShopBannerReference(
            val deleted: Boolean,
            val filename: String?,
            val id: Int?,
            val type: String?,
            val url: String?
        )

        data class ShopOwner(
            val active: Boolean,
            val deleted: Boolean,
            val first_name: String?,
            val gstInfoReference: GstInfoReference,
            val id: Int?,
            val mobile_number: String?,
            val panReference: PanReference,
            val password: String?
        ) {
            data class GstInfoReference(
                val deleted: Boolean,
                val filename: String?,
                val id: Int?,
                val type: String?,
                val url: String?
            )

            data class PanReference(
                val deleted: Boolean,
                val filename: String?,
                val id: Int?,
                val type: String?,
                val url: String?
            )
        }
    }
}