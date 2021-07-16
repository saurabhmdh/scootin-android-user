package com.scootin.view.vo

import android.os.Parcelable
import androidx.annotation.Keep
import com.scootin.network.response.SearchProductsByCategoryResponse
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class ProductSearchVO(
    val id: Int?,
    val price: Double?,
    val discountPrice: Double?,
    val title: String?,
    val description: String?,
    val quantity: Int?,
    val activeForOrder: Boolean,
    val productImage: String?,
    var displayQuantity: Int,
    var inventoryTypes: List<String>?,
    var shopName:String
): Parcelable {
    constructor(network: SearchProductsByCategoryResponse) : this(
        network.id,
        network.price,
        network.discountPrice,
        network.title,
        network.description,
        network.quantity,
        network.shopManagement.shopActiveForOrders,
        network.productImage?.url,
        0,
        network.inventoryTypes,
        network.shopManagement.name
    )
}
