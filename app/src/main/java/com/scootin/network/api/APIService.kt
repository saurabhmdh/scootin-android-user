package com.scootin.network.api

import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.RequestFCM
import com.scootin.network.request.RequestSearch
import com.scootin.network.response.SearchProductsByCategoryResponse
import com.scootin.network.response.SearchShopsByCategoryResponse
import com.scootin.network.response.home.HomeResponseCategory
import com.scootin.network.response.home.ResponseServiceArea
import com.scootin.network.response.login.ResponseUser
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @POST("auth/login/user")
    suspend fun doLogin(@Body options: Map<String, String>, @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()): Response<ResponseUser>

    @GET("category/get-all-shop-category")
    suspend fun getHomeCategory(@HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()): Response<List<HomeResponseCategory>>

    @POST("auth/generate-otp")
    suspend fun requestOTP(@Body options: Map<String, String>, @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()) : Response<ResponseBody>

    @POST("/auth/refresh/user")
    suspend fun refreshToken(@Body options: Map<String, String>, @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()): Response<ResponseUser>


    @POST("/service-area/find-by-location")
    suspend fun findServiceArea(@Body options: Map<String, String>, @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()): Response<ResponseServiceArea>


    @POST("/search/{serviceAreaId}/{categoryId}/get-all-shops")
    suspend fun findShops(
        @Path("serviceAreaId") serviceAreaId: String,
        @Path("categoryId") categoryId: String,
        @Body requestSearch: RequestSearch
    ): Response<List<SearchShopsByCategoryResponse>>


    @POST("/search/{serviceAreaId}/{categoryId}/get-all-products")
    suspend fun findProducts(
        @Path("serviceAreaId") serviceAreaId: String,
        @Path("categoryId") categoryId: String,
        @Body requestSearch: RequestSearch
    ): Response<List<SearchProductsByCategoryResponse>>


    @POST("notification/user/{id}/update-fcm")
    suspend fun updateFCMID(@Path("id") userMobileNumber: String, @Body requestFCM: RequestFCM): Response<ResponseUser>
}