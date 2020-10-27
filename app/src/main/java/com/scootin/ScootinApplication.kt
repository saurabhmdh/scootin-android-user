package com.scootin

import android.content.Context
import android.os.StrictMode
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import androidx.work.Configuration
import androidx.work.WorkManager

import com.scootin.di.DaggerAwareWorkerFactory
import com.scootin.services.StartUpService
import com.scootin.util.ReleaseTree
import dagger.hilt.android.HiltAndroidApp

import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ScootinApplication: MultiDexApplication() {


    @Inject
    lateinit var daggerAwareWorkerFactory: DaggerAwareWorkerFactory

    @Inject
    lateinit var service: StartUpService

    override fun onCreate() {
//        updateStrictPolicy()
        super.onCreate()
        initTimber()

        configureWorkManager()
        service.start()

    }

    private fun initTimber() {
        if (BuildConfig.DEBUG ) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
    }

    private fun configureWorkManager() {
        val config = Configuration.Builder()
            .setWorkerFactory(daggerAwareWorkerFactory)
            .build()
        WorkManager.initialize(this, config)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun updateStrictPolicy() {
        if(BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
//                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build()
            )
        }
    }
}