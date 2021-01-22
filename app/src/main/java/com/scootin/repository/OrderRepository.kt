package com.scootin.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
import com.scootin.network.request.*
import com.scootin.network.response.citywide.CityWideOrderResponse
import com.scootin.network.response.inorder.InOrderDetail
import com.scootin.network.response.order.OrderHistoryItem
import com.scootin.network.response.orderdetail.OrderDetail
import com.scootin.network.response.orders.DirectOrderResponse
import com.scootin.pages.OrderDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Singleton
class OrderRepository @Inject constructor(
    private val services: APIService
) {

    suspend fun checkOutOrder(userId: String, request: PromoCodeRequest) = services.checkOutOrder(userId, request)

    fun userConfirmOrder(
        userId: String,
        orderRequest: OrderRequest,
        context: CoroutineContext
    ): LiveData<Resource<OrderDetail>> =
        object : NetworkBoundResource<OrderDetail>(context) {
            override suspend fun createCall(): Response<OrderDetail> =
                services.userConfirmOrder(userId, orderRequest)
        }.asLiveData()


    fun changePaymentMethod(
        orderId: String,
        context: CoroutineContext
    ): LiveData<Resource<OrderDetail>> =
        object : NetworkBoundResource<OrderDetail>(context) {
            override suspend fun createCall(): Response<OrderDetail> =
                services.changePaymentMethod(orderId)
        }.asLiveData()

    fun userConfirmOrderDirect(
        orderId: String,
        orderRequest: OrderRequest,
        context: CoroutineContext
    ): LiveData<Resource<DirectOrderResponse>> =
        object : NetworkBoundResource<DirectOrderResponse>(context) {
            override suspend fun createCall(): Response<DirectOrderResponse> =
                services.userConfirmOrderDirect(orderId, orderRequest)
        }.asLiveData()


    fun userConfirmOrderCityWide(
        orderId: String,
        orderRequest: OrderRequest,
        context: CoroutineContext
    ): LiveData<Resource<CityWideOrderResponse>> =
        object : NetworkBoundResource<CityWideOrderResponse>(context) {
            override suspend fun createCall(): Response<CityWideOrderResponse> =
                services.userConfirmOrderCityWide(orderId, orderRequest)
        }.asLiveData()


    fun userCancelOrder(
        orderId: String,
        request: CancelOrderRequest,
        context: CoroutineContext
    ): LiveData<Resource<String>> =
        object : NetworkBoundResource<String>(context){
            override suspend fun createCall(): Response<String> =
                services.userCancelOrder(orderId,request)
        }.asLiveData()



    fun getAllOrdersForUser(id: String): Flow<PagingData<OrderHistoryItem>> {
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20)) {
            OrderDataSource(services, id)
        }.flow
    }


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

    fun getCityWideOrder(
        orderId: String,
        context: CoroutineContext
    ): LiveData<Resource<CityWideOrderResponse>> = object : NetworkBoundResource<CityWideOrderResponse>(context) {
        override suspend fun createCall(): Response<CityWideOrderResponse> =
            services.getCityWideOrder(orderId)
    }.asLiveData()

    fun getOrder(
        orderId: String,
        context: CoroutineContext
    ): LiveData<Resource<InOrderDetail>> = object : NetworkBoundResource<InOrderDetail>(context) {
        override suspend fun createCall(): Response<InOrderDetail> =
            services.getOrder(orderId)
    }.asLiveData()


    suspend fun placeCityWideOrder(userId: String, request: CityWideOrderRequest) = services.placeCityWideOrder(userId, request)

    suspend fun checkOrder(orderId: String) = services.checkOrder(orderId)
}