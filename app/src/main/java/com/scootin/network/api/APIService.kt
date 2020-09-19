package com.scootin.network.api

import com.scootin.network.manager.AppHeaders
import com.scootin.network.response.categories.HomeResponseCategory
import com.scootin.network.response.login.ResponseUser
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface APIService {

    @POST("auth/login/user")
    suspend fun doLogin(@Body options: Map<String, String>, @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()): Response<ResponseUser>

    @GET("category/get-all-shop-category")
    suspend fun getHomeCategory(@HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()): Response<List<HomeResponseCategory>>

    @POST("auth/generate-otp")
    suspend fun requestOTP(@Body options: Map<String, String>, @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()) : Response<ResponseBody>

    @POST("/auth/refresh/user")
    suspend fun refreshToken(@Body options: Map<String, String>, @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()): Response<ResponseUser>
}