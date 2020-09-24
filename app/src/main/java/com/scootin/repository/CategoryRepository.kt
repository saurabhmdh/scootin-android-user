package com.scootin.repository

import androidx.lifecycle.LiveData
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
import com.scootin.network.response.home.HomeResponseCategory
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Singleton
class CategoryRepository @Inject constructor(
    private val services: APIService
) {
    fun getHomeCategory(
        context: CoroutineContext
    ): LiveData<Resource<List<HomeResponseCategory>>> = object : NetworkBoundResource<List<HomeResponseCategory>>(context) {
        override suspend fun createCall(): Response<List<HomeResponseCategory>> = services.getHomeCategory()
    }.asLiveData()
}