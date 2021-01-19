package com.scootin.services

import android.app.Application
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.libraries.places.api.Places
import com.scootin.R
import com.scootin.database.dao.CacheDao
import com.scootin.database.dao.LocationDao
import com.scootin.di.IWorkerFactory
import com.scootin.network.AppExecutors
import com.scootin.util.constants.AppConstants
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import javax.inject.Inject

class StartupDataBaseWorker (
    private val context: Application,
    params: WorkerParameters,
    val locationDao: LocationDao,
    val cacheDao: CacheDao
): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = coroutineScope {
        //Lets delete old location data
        locationDao.clearAll()
        //Clear some cache data
        cacheDao.deleteCache(AppConstants.FCM_ID)
        Places.initialize(context, context.getString(R.string.google_maps_key))

        Timber.i("Running Successfully..")
        Result.success()
    }


    /* Factory implementation for Work Manager */
    class Factory @Inject constructor(
        val appExecutors: AppExecutors,
        val locationDao: LocationDao,
        val application: Application,
        val cacheDao: CacheDao
    ) : IWorkerFactory<StartupDataBaseWorker> {

        override fun create(params: WorkerParameters): StartupDataBaseWorker {
            return StartupDataBaseWorker(application, params, locationDao, cacheDao)
        }
    }
}