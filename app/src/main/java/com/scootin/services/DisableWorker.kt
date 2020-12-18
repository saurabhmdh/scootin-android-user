package com.scootin.services

import android.app.Application
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.scootin.database.dao.CacheDao
import com.scootin.di.IWorkerFactory
import com.scootin.network.AppExecutors
import com.scootin.util.constants.AppConstants
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import javax.inject.Inject

class DisableWorker (
    private val context: Application,
    val cacheDao: CacheDao,
    params: WorkerParameters
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        Timber.i("Deleted cache  ..")
        cacheDao.deleteCache(AppConstants.USER_INFO)
        Result.success()
    }

    /* Factory implementation for Work Manager */
    class Factory @Inject constructor(
        val appExecutors: AppExecutors,
        val application: Application,
        val cacheDao: CacheDao
    )
        : IWorkerFactory<DisableWorker> {

        override fun create(params: WorkerParameters): DisableWorker {
            return DisableWorker(application, cacheDao, params)
        }
    }
}