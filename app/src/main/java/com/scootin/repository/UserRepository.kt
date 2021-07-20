package com.scootin.repository

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scootin.database.dao.CacheDao
import com.scootin.database.table.Cache
import com.scootin.network.api.*
import com.scootin.network.manager.AppHeaders
import com.scootin.network.request.AddAddressRequest
import com.scootin.network.request.DistanceMeasure
import com.scootin.network.request.RequestFCM
import com.scootin.network.request.RequestRegisterUser
import com.scootin.network.response.AddressDetails
import com.scootin.network.response.State
import com.scootin.network.response.login.ResponseUser
import com.scootin.util.constants.AppConstants
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class UserRepository @Inject constructor(
    private val services: APIService,
    private val cacheDao: CacheDao
) {
    fun doLogin(
        options: Map<String, String>,
        context: CoroutineContext
    ): LiveData<Resource<ResponseUser>> = object : NetworkBoundResource<ResponseUser>(context) {
        override suspend fun createCall(): Response<ResponseUser> = services.doLogin(options)
    }.asLiveData()

    fun registerUser(
        request: RequestRegisterUser,
        context: CoroutineContext
    ): LiveData<Resource<ResponseUser>> = object : NetworkBoundResource<ResponseUser>(context) {
        override suspend fun createCall(): Response<ResponseUser> = services.registerUser(request)
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

    suspend fun deleteAddress(userId: String, addressId: String) = services.deleteAddress(userId, addressId)

    suspend fun addNewAddress(address: AddAddressRequest) = services.addNewAddress(AppHeaders.userID, address)

    suspend fun getAllAddress() = services.getAllAdress(AppHeaders.userID)

    suspend fun getAllServiceArea() = services.getAllServiceArea()


    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.HOURS)

    private val KEY = "state_info"

    fun getAllState(coroutineContext: CoroutineContext): LiveData<out Resource<List<State>>> = object : CacheNetworkBoundResource<List<State>>(coroutineContext) {
        override suspend fun saveCallResult(item: List<State>) {
            cacheDao.insert(Cache(AppConstants.STATE_INFO,  Gson().toJson(item)))
        }

        override fun shouldFetch(data: List<State>?): Boolean {
            val timeout = repoListRateLimit.shouldFetch(KEY)
            return data == null || timeout
        }

        override suspend fun loadFromDb(): List<State>? {
            val data = cacheDao.getCacheData(AppConstants.STATE_INFO)

            return if (data == null) null else {
                val decode = decode(data.value)
                decode
            }
        }

        override suspend fun createCall(): Response<List<State>> = services.getAllState()
    }.asLiveData()

    fun decode(data: String): List<State>? {
        val listType = object : TypeToken<List<State>>() {}.type
        return  Gson().fromJson<List<State>>(data, listType)
    }


    suspend fun findDistance(request: DistanceMeasure) = services.findDistance(request)
}