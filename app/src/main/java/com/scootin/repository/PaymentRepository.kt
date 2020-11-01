package com.scootin.repository

import androidx.lifecycle.LiveData
import com.scootin.database.dao.CacheDao
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
import okhttp3.ResponseBody
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

}