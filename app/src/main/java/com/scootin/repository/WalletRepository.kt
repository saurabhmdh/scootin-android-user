package com.scootin.repository

import androidx.lifecycle.LiveData
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
import com.scootin.network.request.AddMoneyWallet
import com.scootin.network.request.VerifyAmountRequest
import com.scootin.network.response.wallet.AddWalletResponse
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


    fun addMoney(
        userId: String,
        addMoneyWallet: AddMoneyWallet,
        context: CoroutineContext
    ): LiveData<Resource<AddWalletResponse>> = object : NetworkBoundResource<AddWalletResponse>(context) {
        override suspend fun createCall(): Response<AddWalletResponse> = services.addMoney(userId, addMoneyWallet)
    }.asLiveData()



    fun verifyWalletPayment(
        userId: String,
        verifyRequest: VerifyAmountRequest,
        context: CoroutineContext
    ): LiveData<Resource<String>> = object : NetworkBoundResource<String>(context) {
        override suspend fun createCall(): Response<String> = services.verifyWalletPayment(userId, verifyRequest)
    }.asLiveData()
}