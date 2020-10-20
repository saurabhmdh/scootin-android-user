package com.scootin.network.api

import com.scootin.network.request.AddToCartRequest
import com.scootin.network.request.CapturePaymentRequest
import com.scootin.network.request.PlaceOrderRequest
import com.scootin.network.response.TempleInfo
import retrofit2.Response
import retrofit2.http.GET

interface APIService {

    @GET("get-all")
    suspend fun getAllTemples(): Response<List<TempleInfo>>

    @GET("/order/capture-payment")
    suspend fun capturePayment(): Response<CapturePaymentRequest>

    @GET("/cart/add-to-cart")
    suspend fun addToCart(): Response<AddToCartRequest>

    @GET("/order/orders/count-total")
    suspend fun countTotal()

    @GET("/order/place-order")
    suspend fun placeOrder(): Response<PlaceOrderRequest>
}