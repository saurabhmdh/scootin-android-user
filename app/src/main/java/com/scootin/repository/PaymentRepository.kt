package com.scootin.repository

import androidx.lifecycle.LiveData
import com.scootin.database.dao.CacheDao
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
import com.scootin.network.request.PromoCodeRequest
import com.scootin.network.request.VerifyAmountRequest
import com.scootin.network.response.citywide.CityWideOrderResponse
import com.scootin.network.response.orders.DirectOrderResponse
import com.scootin.network.response.placeOrder.PlaceOrderResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Singleton
class PaymentRepository @Inject constructor(
    private val services: APIService,
    private val cacheDao: CacheDao
) {

    suspend fun getTotalPriceFromCart(userId: String) = services.getTotalPriceFromCart(userId)

    suspend fun applyPromoCode(orderId: String, userId: String, promoCodeRequest: PromoCodeRequest) = services.applyPromoCode(orderId, userId, promoCodeRequest)

//    suspend fun verifyPayment(verifyAmountRequest: VerifyAmountRequest) = services.verifyPayment(verifyAmountRequest)

    fun verifyPayment(
        verifyAmountRequest: VerifyAmountRequest,
        context: CoroutineContext
    ): LiveData<Resource<PlaceOrderResponse>> = object : NetworkBoundResource<PlaceOrderResponse>(context) {
        override suspend fun createCall(): Response<PlaceOrderResponse> = services.verifyPayment(verifyAmountRequest)
    }.asLiveData()


    fun verifyPaymentDirect(
        verifyAmountRequest: VerifyAmountRequest,
        context: CoroutineContext
    ): LiveData<Resource<DirectOrderResponse>> = object : NetworkBoundResource<DirectOrderResponse>(context) {
        override suspend fun createCall(): Response<DirectOrderResponse> = services.verifyPaymentDirect(verifyAmountRequest)
    }.asLiveData()


    fun verifyPaymentCityWide(
        verifyAmountRequest: VerifyAmountRequest,
        context: CoroutineContext
    ): LiveData<Resource<CityWideOrderResponse>> = object : NetworkBoundResource<CityWideOrderResponse>(context) {
        override suspend fun createCall(): Response<CityWideOrderResponse> = services.verifyPaymentCityWide(verifyAmountRequest)
    }.asLiveData()
}