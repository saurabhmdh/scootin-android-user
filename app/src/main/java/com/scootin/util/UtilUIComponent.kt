package com.scootin.util


import com.scootin.network.response.AddressDetails
import java.lang.StringBuilder

object UtilUIComponent {

    fun setOneLineAddress(address: AddressDetails?): String {
        if (address == null) return ""
        val sb = StringBuilder()
        if (address.name.isNullOrEmpty().not()) {
            sb.append(address.name).append(", ")
        }
        if (address.mobileNumber.isNullOrEmpty().not()) {
            sb.append(address.mobileNumber).append(", ")
        }
        if (address.email.isNullOrEmpty().not()) {
            sb.append(address.email).append(", ")
        }
        sb.append(address.addressLine1).append(", ")
        sb.append(address.addressLine2).append(", ")
        sb.append(address.city).append(", ")
        sb.append(address.pincode)
        return sb.toString()
    }
}