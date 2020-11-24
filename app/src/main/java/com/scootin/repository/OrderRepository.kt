package com.scootin.repository

import androidx.lifecycle.LiveData
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.CityWideOrderRequest
import com.scootin.network.request.DirectOrderRequest
import com.scootin.network.request.OrderRequest
import com.scootin.network.request.PlaceOrderRequest
import com.scootin.network.response.citywide.CityWideOrderResponse
import com.scootin.network.response.inorder.InOrderDetail
import com.scootin.network.response.order.OrderHistoryItem
import com.scootin.network.response.orderdetail.OrderDetail
import com.scootin.network.response.orders.DirectOrderResponse
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
    ): LiveData<Resource<PlaceOrderResponse>> =
        object : NetworkBoundResource<PlaceOrderResponse>(context) {
            override suspend fun createCall(): Response<PlaceOrderResponse> =
                services.placeOrder(userId, placeOrderRequest)
        }.asLiveData()


    fun userConfirmOrder(
        orderId: String,
        userId: String,
        orderRequest: OrderRequest,
        context: CoroutineContext
    ): LiveData<Resource<PlaceOrderResponse>> =
        object : NetworkBoundResource<PlaceOrderResponse>(context) {
            override suspend fun createCall(): Response<PlaceOrderResponse> =
                services.userConfirmOrder(orderId, userId, orderRequest)
        }.asLiveData()


    fun getAllOrdersForUser(
        context: CoroutineContext
    ): LiveData<Resource<List<OrderHistoryItem>>> =
        object : NetworkBoundResource<List<OrderHistoryItem>>(context) {
            override suspend fun createCall(): Response<List<OrderHistoryItem>> =
                services.getAllOrdersForUser(AppHeaders.userID)
        }.asLiveData()


    fun placeDirectOrder(
        userId: String,
        request: DirectOrderRequest,
        context: CoroutineContext
    ): LiveData<Resource<DirectOrderResponse>> =
        object : NetworkBoundResource<DirectOrderResponse>(context) {
            override suspend fun createCall(): Response<DirectOrderResponse> =
                services.placeDirectOrder(userId, request)
        }.asLiveData()

    fun getDirectOrder(
        orderId: String,
        context: CoroutineContext
    ): LiveData<Resource<OrderDetail>> = object : NetworkBoundResource<OrderDetail>(context) {
        override suspend fun createCall(): Response<OrderDetail> =
            services.getDirectOrder(orderId)
    }.asLiveData()

    fun getOrder(
        orderId: String,
        context: CoroutineContext
    ): LiveData<Resource<InOrderDetail>> = object : NetworkBoundResource<InOrderDetail>(context) {
        override suspend fun createCall(): Response<InOrderDetail> =
            services.getOrder(orderId)
    }.asLiveData()


    suspend fun placeCityWideOrder(userId: String, request: CityWideOrderRequest) = services.placeCityWideOrder(userId, request)
}