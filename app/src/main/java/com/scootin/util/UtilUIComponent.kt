package com.scootin.util


import com.scootin.network.response.AddressDetails
import java.lang.StringBuilder

object UtilUIComponent {

    fun setOneLineAddress(address: AddressDetails?): String {
        if (address == null) return ""
        val sb = StringBuilder().append(address.addressLine1).append(", ")
            .append(", ").append(address.city).append(", ")
            .append(address.pincode)
        return sb.toString()
    }
}