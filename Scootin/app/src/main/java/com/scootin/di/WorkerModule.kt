package com.scootin.di

import androidx.work.ListenableWorker
import com.scootin.services.StartupDataBaseWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(StartupDataBaseWorker::class)
    fun bindDatabaseWorker(factory: StartupDataBaseWorker.Factory): IWorkerFactory<out ListenableWorker>

}