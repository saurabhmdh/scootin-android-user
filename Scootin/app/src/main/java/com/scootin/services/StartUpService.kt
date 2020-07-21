package com.scootin.services

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import timber.log.Timber
import javax.inject.Inject

class StartUpService @Inject constructor() {

    fun start() {
        WorkManager.getInstance().enqueue(OneTimeWorkRequestBuilder<StartupDataBaseWorker>().build())
        Timber.i("Startup service completed his job!!")
    }
}