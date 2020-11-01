package com.scootin.repository

import androidx.lifecycle.LiveData
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
import com.scootin.network.request.OrderRequest
import com.scootin.network.request.PlaceOrderRequest
import com.scootin.network.response.placeOrder.PlaceOrderResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Singleton
class OrderRepository @Inject constructor(
    private val services: APIService
) {

    fun placeOrder(
        userId: String,
        placeOrderRequest: PlaceOrderRequest,
        context: CoroutineContext
    ): LiveData<Resource<PlaceOrderResponse>> = object : NetworkBoundResource<PlaceOrderResponse>(context) {
        override suspend fun createCall(): Response<PlaceOrderResponse> = services.placeOrder(userId, placeOrderRequest)
    }.asLiveData()


    fun userConfirmOrder(
        orderId: String,
        userId: String,
        orderRequest: OrderRequest,
        context: CoroutineContext
    ): LiveData<Resource<PlaceOrderResponse>> = object : NetworkBoundResource<PlaceOrderResponse>(context) {
        override suspend fun createCall(): Response<PlaceOrderResponse> = services.userConfirmOrder(orderId, userId, orderRequest)
    }.asLiveData()

}