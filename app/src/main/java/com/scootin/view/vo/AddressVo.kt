package com.scootin.view.vo

import android.os.Parcelable
import androidx.annotation.Keep
import com.scootin.network.response.AddressDetails
import com.scootin.network.response.StateDetails
import kotlinx.android.parcel.Parcelize


@Keep
@Parcelize
data class AddressVo (
    val id: Long,
    val addressLine1: String,
    val addressLine2: String?,
    val addressType: String,
    val city: String,
    val hasDefault: Boolean,
    val pincode: String,
    val stateDetails: StateDetails?,
    var selected: Boolean,
    val addressDetail: AddressDetails?
): Parcelable {
    constructor(address: AddressDetails) : this(
        address.id,
        address.addressLine1,
        address.addressLine2,
        address.addressType,
        address.city,
        address.hasDefault,
        address.pincode,
        address.stateDetails,
        address.hasDefault, //By default defaulted item to be selected.
        address
    )
}