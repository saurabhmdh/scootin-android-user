package com.scootin.repository

import androidx.lifecycle.LiveData
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.AddToCartRequest
import com.scootin.network.request.RequestSearch
import com.scootin.network.response.SearchShopsByCategoryResponse
import okhttp3.MultipartBody
import retrofit2.Response
import java.io.File
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

    suspend fun addToCart(request: AddToCartRequest) = services.addToCart(request)

    suspend fun getUserCartList(userId: String) = services.getUserCartList(userId)

    suspend fun addMoney() = services.addMoney()

    suspend fun listTransaction() = services.listTransaction()

    suspend fun findProductFromShop(shopId: Int) = services.findProductFromShop(shopId)

    suspend fun uploadImage(filePart: MultipartBody.Part) = services.uploadImage(filePart)

    /*  fun uploadImage(context: CoroutineContext, filePath: String, uploadCallbacks: UploadCallbacks) =
          object : NetworkBoundResource<MediaServerResponse>(context) {
              val file = File(filePath)
              val fileBody = ProgressRequestBody(file, "multipart/form-data", uploadCallbacks)
              val filePart =
                  MultipartBody.Part.createFormData("media", file.name, fileBody)

              override suspend fun createCall(): Response<MediaServerResponse> =
                  services.uploadImage(filePart)
          }.asLiveData()*/

}