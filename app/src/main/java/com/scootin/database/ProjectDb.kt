package com.scootin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.scootin.BuildConfig
import com.scootin.database.dao.AddressDao
import com.scootin.database.dao.CacheDao
import com.scootin.database.dao.LocationDao
import com.scootin.database.dao.StateDao
import com.scootin.database.table.Address
import com.scootin.database.table.Cache
import com.scootin.database.table.EntityLocation
import com.scootin.database.table.State

@Database(
    entities = [
        Cache::class,
        EntityLocation::class,
        Address::class,
        State::class
    ], version = BuildConfig.DB_VERSION, exportSchema = false
)

abstract class ProjectDb : RoomDatabase() {
    abstract fun cacheDao(): CacheDao
    abstract fun locationDao(): LocationDao
    abstract fun addressDao(): AddressDao
    abstract fun StateDao(): StateDao
}