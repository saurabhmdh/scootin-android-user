package com.scootin.network.api

import com.scootin.network.response.TempleInfo
import retrofit2.Response
import retrofit2.http.GET

interface APIService {

    @GET("get-all")
    suspend fun getAllTemples(): Response<List<TempleInfo>>
}