package com.scootin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.scootin.BuildConfig
import com.scootin.database.dao.CacheDao
import com.scootin.database.dao.LocationDao
import com.scootin.database.table.Cache
import com.scootin.database.table.EntityLocation

@Database(
    entities = [
        Cache::class,
        EntityLocation::class
    ], version = BuildConfig.DB_VERSION, exportSchema = false
)

abstract class ProjectDb : RoomDatabase() {
    abstract fun cacheDao(): CacheDao
    abstract fun locationDao(): LocationDao
}