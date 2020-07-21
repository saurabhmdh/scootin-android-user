package com.scootin.repository

import androidx.lifecycle.LiveData
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
import com.scootin.network.response.TempleInfo
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TempleRepo @Inject constructor(
    private val services: APIService
) {

    fun getAllTemples(
        context: CoroutineContext
    ): LiveData<Resource<List<TempleInfo>>> = object : NetworkBoundResource<List<TempleInfo>>(context) {
        override suspend fun createCall(): Response<List<TempleInfo>> = services.getAllTemples()
    }.asLiveData()
}