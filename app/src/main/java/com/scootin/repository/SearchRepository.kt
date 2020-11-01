package com.scootin.repository

import com.scootin.network.api.APIService
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.*
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SearchRepository @Inject constructor(
    private val services: APIService
) {

//    fun searchQuery(
//        context: CoroutineContext,
//        requestSearch: RequestSearch,
//        serviceAreaId: String,
//        categoryId: String
//    ): LiveData<Resource<List<SearchShopsByCategoryResponse>>> = object : NetworkBoundResource<List<SearchShopsByCategoryResponse>>(context) {
//        override suspend fun createCall(): Response<List<SearchShopsByCategoryResponse>> = services.findShop(serviceAreaId, categoryId, requestSearch)
//    }.asLiveData()

    suspend fun searchShops(
        requestSearch: RequestSearch,
        serviceAreaId: String,
        categoryId: String
    ) = services.findShops(serviceAreaId, categoryId, requestSearch)

    suspend fun searchProducts(query: String, serviceAreaId: String, categoryId: String) =
        services.findProducts(serviceAreaId, categoryId, RequestSearch(query = query))

    suspend fun addToCart(request: AddToCartRequest) = services.addToCart(request)

    suspend fun getUserCartList(userId: String) = services.getUserCartList(userId)

    suspend fun addMoney() = services.addMoney()

    suspend fun listTransaction() = services.listTransaction()

    suspend fun findProductFromShop(shopId: Long, query: String) = services.findProductFromShop(shopId, RequestSearch(query = query))

    suspend fun uploadImage(filePart: MultipartBody.Part) = services.uploadImage(filePart)


    suspend fun userConfirmOrder(orderId: Int, orderRequest: OrderRequest) =
        services.userConfirmOrder(orderId, AppHeaders.userID.toInt(), orderRequest)

    suspend fun applyPromoCode(orderId: Int, promoCodeRequest: PromoCodeRequest) =
        services.applyPromoCode(orderId, AppHeaders.userID.toInt(), promoCodeRequest)

    suspend fun verifyPayment(verifyAmountRequest: VerifyAmountRequest) =
        services.verifyPayment(verifyAmountRequest)
}