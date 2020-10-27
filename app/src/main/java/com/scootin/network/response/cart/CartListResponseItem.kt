package com.scootin.network.response.cart

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartListResponseItem(
    val deleted: Boolean,
    val hasOrdered: Boolean,
    val id: Int?,
    val inventoryDetails: InventoryDetails,
    val quantity: Int?,
    val userInfo: UserInfoX
) : Parcelable {

    @Parcelize
    data class InventoryDetails(
        val active: Boolean,
        val categoryDetails: CategoryDetails,
        val currentQuantity: Int?,
        val description: String?,
        val id: Int?,
        val price: Double,
        val productImage: ProductImage,
        val quantity: Int?,
        val serviceID: ServiceIDX,
        val shopManagement: ShopManagement,
        val title: String?
    ) : Parcelable

    @Parcelize
    data class ServiceIDX(
        val deleted: Boolean,
        val id: Int?,
        val latitude: Double,
        val longitude: Double,
        val name: String?,
        val serviceRadius: Long
    ) : Parcelable

    @Parcelize

    data class ShopManagement(
        val address: Address,
        val amount: Double,
        val categoryDetails: CategoryDetails,
        val closeTime: String?,
        val createdAt: Long?,
        val deleted: Boolean,
        val discountType: String?,
        val id: Int?,
        val latitude: Double,
        val longitude: Double,
        val modified: Long,
        val name: String?,
        val openTime: String?,
        val serviceID: ServiceIDX,
        val shopBannerReference: ShopBannerReference,
        val shopLandLineNumber: String?,
        val shopOwner: ShopOwner,
        val status: Boolean
    ) : Parcelable


    @Parcelize
    data class ProductImage(
        val deleted: Boolean,
        val filename: String?,
        val id: Int?,
        val thumb: String?,
        val type: String?,
        val url: String?
    ) : Parcelable

    @Parcelize
    data class CategoryDetails(
        val active: Boolean,
        val deleted: Boolean,
        val description: String?,
        val id: Int?,
        val name: String?
    ) : Parcelable

    @Parcelize
    data class UserInfoX(
        val active: Boolean,
        val createdAt: Long,
        val deleted: Boolean,
        val email: String?,
        val fcmId: String?,
        val firstName: String?,
        val id: Int?,
        val lastName: String?,
        val mobileNumber: String?,
        val modified: Long,
        val otp: String?,
        val otpExpireTime: Long,
        val password: String?,
        val profilePicture: String?,
        val walletInfoDetails: WalletInfoDetails
    ) : Parcelable

    @Parcelize
    data class WalletInfoDetails(
        val balance: Double?,
        val currency: String?,
        val id: Int?,
        val lastUpdated: String?,
        val lastUpdatedBy: String?
    ) : Parcelable

    @Parcelize
    data class Address(
        val addressLine1: String?,
        val addressLine2: String?,
        val addressType: String?,
        val city: String?,
        val deleted: Boolean,
        val hasDefault: Boolean,
        val id: Int?,
        val pincode: String?,
        val stateDetails: StateDetails?,
        val userInfo: UserInfoX?
    ) : Parcelable

    @Parcelize
    data class ShopBannerReference(
        val deleted: Boolean,
        val filename: String?,
        val id: Int?,
        val thumb: String?,
        val type: String?,
        val url: String?
    ) : Parcelable

    @Parcelize
    data class StateDetails(
        val countryDetails: CountryDetails,
        val id: Int?,
        val name: String?
    ) : Parcelable

    @Parcelize
    data class CountryDetails(
        val id: Int?,
        val name: String?
    ) : Parcelable

    @Parcelize
    data class ShopOwner(
        val aadharCardReference: AadharCardReference,
        val aadhar_card: String?,
        val active: Boolean,
        val deleted: Boolean,
        val email: String?,
        val fcm_id: String?,
        val first_name: String?,
        val gstInfoReference: GstInfoReference,
        val gst_info: String?,
        val id: Int?,
        val last_name: String?,
        val mobile_number: String?,
        val panReference: PanReference,
        val pan_card: String?,
        val password: String?
    ) : Parcelable

    @Parcelize
    data class PanReference(
        val deleted: Boolean,
        val filename: String?,
        val id: Int?,
        val thumb: String?,
        val type: String?,
        val url: String?
    ) : Parcelable

    @Parcelize
    data class GstInfoReference(
        val deleted: Boolean,
        val filename: String?,
        val id: Int?,
        val thumb: String?,
        val type: String?,
        val url: String?
    ) : Parcelable

    @Parcelize

    data class AadharCardReference(
        val deleted: Boolean,
        val filename: String?,
        val id: Int?,
        val thumb: String?,
        val type: String?,
        val url: String?
    ) : Parcelable
}