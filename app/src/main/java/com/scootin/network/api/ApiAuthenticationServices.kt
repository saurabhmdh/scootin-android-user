package com.scootin.network.api

import com.scootin.network.manager.AppHeaders
import com.scootin.network.response.login.ResponseUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface ApiAuthenticationServices {

    @POST("/auth/refresh/user")
    fun refreshToken(@Body options: Map<String, String>, @HeaderMap map: Map<String, String> = AppHeaders.getHeaderMap()): Call<ResponseUser>
}