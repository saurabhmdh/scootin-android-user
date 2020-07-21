package com.scootin.di

import androidx.work.ListenableWorker
import com.scootin.services.StartupDataBaseWorker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(ApplicationComponent::class)
interface WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(StartupDataBaseWorker::class)
    fun bindDatabaseWorker(factory: StartupDataBaseWorker.Factory): IWorkerFactory<out ListenableWorker>

}