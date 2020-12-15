package com.scootin.util


import android.graphics.Color
import com.scootin.network.response.AddressDetails
import com.scootin.network.response.PaymentDetails
import timber.log.Timber

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

    fun getPaymentStatusText(paymentDetail: PaymentDetails?): String {
        if (paymentDetail?.paymentMode == null) {
            return "PENDING"
        }

        Timber.i("updatePaymentStatus -> ${paymentDetail.paymentStatus}")

        val paymentStatus = if (paymentDetail.paymentStatus == "COMPLETED") {
            (Color.parseColor("#3cb043"))
            "COMPLETED"
        } else {
            "PENDING"
        }

        return paymentStatus + " { ${paymentDetail.paymentMode} }"
    }

}