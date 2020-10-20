package com.scootin.network.api

import androidx.annotation.AnyThread
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.liveData
import com.scootin.util.network.NetworkUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import retrofit2.Response
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class CacheNetworkBoundResource<ResultType> constructor(val context: CoroutineContext) {
    protected open fun onFetchFailed() {}

    @WorkerThread
    fun asLiveData() = liveData(context + handler) {
        if (NetworkUtil.isMainThread()) throw Exception("It should call from background thread")

        emit(Resource.loading(null))
        val data = loadFromDb()

        if (shouldFetch(data)) {
            val apiResponse = createCall()
            if (apiResponse.isSuccessful) {
                val result = apiResponse.body()
                result?.let {
                    saveCallResult(it)
                    emit(Resource.success(apiResponse.code(), it))
                }
            } else {
                onFetchFailed()
                emit(
                    Resource.error(
                        apiResponse.code(),
                        apiResponse.errorBody()?.toString().orEmpty(),
                        null
                    )
                )
            }
        } else {
            emit(Resource.success(data))
        }
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        Timber.i("Caught  $exception")
        onFetchFailed()
    }

    @WorkerThread
    protected abstract suspend fun saveCallResult(item: ResultType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @AnyThread
    protected abstract suspend fun loadFromDb(): ResultType?

    @AnyThread
    protected abstract suspend fun createCall(): Response<ResultType>
}