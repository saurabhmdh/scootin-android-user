package com.scootin.repository

import androidx.lifecycle.LiveData
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
import com.scootin.network.response.wallet.WalletTransactionResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Singleton
class WalletRepository @Inject constructor(
    private val services: APIService
) {

    fun listTransaction(
        userId: String,
        context: CoroutineContext
    ): LiveData<Resource<List<WalletTransactionResponse>>> = object : NetworkBoundResource<List<WalletTransactionResponse>>(context) {
        override suspend fun createCall(): Response<List<WalletTransactionResponse>> = services.listTransaction(userId)
    }.asLiveData()

    suspend fun addMoney() = services.addMoney()
}