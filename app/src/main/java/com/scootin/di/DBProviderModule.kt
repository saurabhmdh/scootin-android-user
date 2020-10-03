package com.scootin.di

import android.app.Application
import androidx.room.Room
import com.scootin.database.ProjectDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DBProviderModule {
    @Singleton
    @Provides
    fun provideDb(app: Application): ProjectDb = Room.databaseBuilder(
        app,
        ProjectDb::class.java,
        "scootin.db"
    ).fallbackToDestructiveMigration()
//        .addMigrations(MIGRATION_2_1)
        .build()


    @Singleton
    @Provides
    fun provideCacheDao(db: ProjectDb) = db.cacheDao()

    @Singleton
    @Provides
    fun provideLocationDao(db: ProjectDb) = db.locationDao()
}