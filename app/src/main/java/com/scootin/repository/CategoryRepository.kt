package com.scootin.repository

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scootin.database.dao.CacheDao
import com.scootin.database.table.Cache
import com.scootin.network.api.*
import com.scootin.network.response.home.HomeResponseCategory
import com.scootin.util.constants.AppConstants
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Singleton
class CategoryRepository @Inject constructor(
    private val services: APIService,
    private val cacheDao: CacheDao
) {
    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.HOURS)

    private val KEY = "special_feature"

    fun getHomeCategory(coroutineContext: CoroutineContext): LiveData<out Resource<List<HomeResponseCategory>>> = object : CacheNetworkBoundResource<List<HomeResponseCategory>>(coroutineContext) {
        override suspend fun saveCallResult(item: List<HomeResponseCategory>) {
            cacheDao.insert(Cache(AppConstants.CATEGORY_INFO,  Gson().toJson(item)))
        }

        override fun shouldFetch(data: List<HomeResponseCategory>?): Boolean {
            val timeout = repoListRateLimit.shouldFetch(KEY)
            return data == null || timeout
        }

        override suspend fun loadFromDb(): List<HomeResponseCategory>? {
            val data = cacheDao.getCacheData(AppConstants.CATEGORY_INFO)

            return if (data == null) null else {
                val decode = decode(data.value)
                decode
            }
        }

        override suspend fun createCall(): Response<List<HomeResponseCategory>> = services.getHomeCategory()
    }.asLiveData()

    fun decode(data: String): List<HomeResponseCategory>? {
        val listType = object : TypeToken<List<HomeResponseCategory>>() {}.type
        return  Gson().fromJson<List<HomeResponseCategory>>(data, listType)
    }
}