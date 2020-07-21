package com.scootin.di

import android.app.Application
import androidx.room.Room
import com.scootin.database.TempleDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DBProviderModule {
    @Singleton
    @Provides
    fun provideDb(app: Application): TempleDb = Room.databaseBuilder(
        app,
        TempleDb::class.java,
        "temple.db"
    ).fallbackToDestructiveMigration()
//        .addMigrations(MIGRATION_2_1)
        .build()
}