package com.scootin.network.request

class CapturePaymentRequest(
    val razorPayPaymentID: String,
    val razorPayorderID: String,
    val razorPaysignature: String,
    val localTransactionID: String
)