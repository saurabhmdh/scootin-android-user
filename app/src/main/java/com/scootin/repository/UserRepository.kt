package com.scootin.repository

import androidx.lifecycle.LiveData
import com.scootin.network.api.APIService
import com.scootin.network.api.NetworkBoundResource
import com.scootin.network.api.Resource
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.RequestFCM
import com.scootin.network.response.Address
import com.scootin.network.response.cart.CartListResponseItem
import com.scootin.network.response.login.ResponseUser
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class UserRepository @Inject constructor(
    private val services: APIService
) {
    fun doLogin(
        options: Map<String, String>,
        context: CoroutineContext
    ): LiveData<Resource<ResponseUser>> = object : NetworkBoundResource<ResponseUser>(context) {
        override suspend fun createCall(): Response<ResponseUser> = services.doLogin(options)
    }.asLiveData()

//    suspend fun sendOTP(options: Map<String, String>) = services.requestOTP(options)

    fun sendOTP(
        options: Map<String, String>,
        context: CoroutineContext
    ): LiveData<Resource<ResponseBody>> = object : NetworkBoundResource<ResponseBody>(context) {
        override suspend fun createCall(): Response<ResponseBody> = services.requestOTP(options)
    }.asLiveData()


    suspend fun updateFCMId(id: String, request: RequestFCM) = services.updateFCMID(id, request)

    suspend fun updateDefaultAddress(addressId: String) = services.updateDefaultAddress(AppHeaders.userID, addressId)

    suspend fun addNewAddress(address: Address) = services.addNewAddress(/*AppHeaders.userID,*/ address)

    suspend fun getAllAddress() = services.getAllAdress(AppHeaders.userID)

}