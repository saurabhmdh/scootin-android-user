package com.scootin.services

import android.app.Application
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.scootin.di.IWorkerFactory
import com.scootin.network.AppExecutors
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import javax.inject.Inject

class StartupDataBaseWorker (
    private val context: Application,
    params: WorkerParameters
): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = coroutineScope {
        Timber.i("Running Successfully..")
        Result.success()
    }


    /* Factory implementation for Work Manager */
    class Factory @Inject constructor(
        val appExecutors: AppExecutors,
        val application: Application)
        : IWorkerFactory<StartupDataBaseWorker> {

        override fun create(params: WorkerParameters): StartupDataBaseWorker {
            return StartupDataBaseWorker(application, params)
        }
    }
}