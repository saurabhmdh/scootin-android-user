package com.scootin.repository

import androidx.lifecycle.LiveData
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
import com.scootin.network.request.RequestSearch
import com.scootin.network.response.SearchShopsByCategoryResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


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

    suspend fun searchShops(requestSearch: RequestSearch, serviceAreaId: String, categoryId: String) = services.findShops(serviceAreaId, categoryId, requestSearch)

    suspend fun searchProducts(query: String, serviceAreaId: String, categoryId: String) = services.findProducts(serviceAreaId, categoryId, RequestSearch(query=query))
}