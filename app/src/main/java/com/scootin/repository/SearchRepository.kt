package com.scootin.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.scootin.network.api.APIService
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.*
import com.scootin.network.response.SearchProductsByCategoryResponse
import com.scootin.network.response.order.OrderHistoryItem
import com.scootin.pages.OrderDataSource
import com.scootin.pages.SearchDataSource
import com.scootin.view.vo.ProductSearchVO
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import timber.log.Timber
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


    suspend fun getUserCartList(userId: String) = services.getUserCartList(userId)


    suspend fun findProductFromShop(shopId: Long, query: String) = services.findProductFromShop(shopId, RequestSearch(query = query))

    suspend fun uploadImage(filePart: MultipartBody.Part) = services.uploadImage(filePart)

    suspend fun getDeliverySlot(currentTime: Long) = services.getDeliverySlot(currentTime)

    fun findProductsWithPaging(query: String, serviceAreaId: String, categoryId: String): Flow<PagingData<ProductSearchVO>> {
        Timber.i("trying to find data with $query, $serviceAreaId, $categoryId")
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20)) {
            Timber.i("trying to find data with $query, $serviceAreaId, $categoryId")
            SearchDataSource(services, serviceAreaId, categoryId, RequestSearch(query = query))
        }.flow
    }
}