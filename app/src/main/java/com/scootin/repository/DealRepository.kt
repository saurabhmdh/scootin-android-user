package com.scootin.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.scootin.network.api.APIService
import com.scootin.network.request.RequestDeals
import com.scootin.network.response.home.DealResponse
import com.scootin.pages.DealDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DealRepository @Inject constructor(
    private val services: APIService
) {

    fun getAllDeals(type: String): Flow<PagingData<DealResponse>> {
        return Pager(config = PagingConfig(pageSize = 20, initialLoadSize = 20, enablePlaceholders = false)) {
            DealDataSource(services, type)
        }.flow
    }

    suspend fun getAllAvaliableDeals(requestDeal: RequestDeals) = services.getDeals(requestDeal)
}