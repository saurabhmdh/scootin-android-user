package com.scootin.network.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.scootin.util.network.NetworkUtil
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

abstract class NetworkBoundResource<ResultType> constructor(val context: CoroutineContext) {

    fun asLiveData(): LiveData<Resource<ResultType>> = liveData(context) {

        if(NetworkUtil.isMainThread()) throw Exception("It should call from background thread")

        emit(Resource.loading(null))
        try {
            val apiResponse = createCall()
            if (apiResponse.isSuccessful) {
                emit(Resource.success(apiResponse.code(), apiResponse.body()))
            } else {
                onFetchFailed()
                emit(Resource.error(apiResponse.code(), apiResponse.errorBody()?.string() ?: "", null))
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message.orEmpty(), null))
        }
    }
    protected open fun onFetchFailed() {}

    protected abstract suspend fun createCall(): Response<ResultType>
}