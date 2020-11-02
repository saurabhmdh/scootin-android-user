package com.scootin.repository

import androidx.lifecycle.LiveData
import com.scootin.database.dao.CacheDao
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
import com.scootin.network.request.PromoCodeRequest
import com.scootin.network.request.VerifyAmountRequest
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Singleton
class PaymentRepository @Inject constructor(
    private val services: APIService,
    private val cacheDao: CacheDao
) {

    fun getTotalPriceFromCart(
        userId: String,
        context: CoroutineContext
    ): LiveData<Resource<Double>> = object : NetworkBoundResource<Double>(context) {
        override suspend fun createCall(): Response<Double> = services.getTotalPriceFromCart(userId)
    }.asLiveData()

    suspend fun applyPromoCode(orderId: String, userId: String, promoCodeRequest: PromoCodeRequest) = services.applyPromoCode(orderId, userId, promoCodeRequest)

//    suspend fun verifyPayment(verifyAmountRequest: VerifyAmountRequest) = services.verifyPayment(verifyAmountRequest)

    fun verifyPayment(
        verifyAmountRequest: VerifyAmountRequest,
        context: CoroutineContext
    ): LiveData<Resource<String>> = object : NetworkBoundResource<String>(context) {
        override suspend fun createCall(): Response<String> = services.verifyPayment(verifyAmountRequest)
    }.asLiveData()
}