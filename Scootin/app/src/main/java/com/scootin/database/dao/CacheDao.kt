package com.scootin.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scootin.database.table.Cache

@Dao
interface CacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cache: Cache)

    @Query("SELECT * FROM cache WHERE `index` = :keyIndex")
    fun getData(keyIndex: String): LiveData<Cache>

    @Query("DELETE FROM cache WHERE `index` = :keyIndex")
    fun delete(keyIndex: String)

    @Query("SELECT * FROM cache WHERE 1")
    fun loadAll(): LiveData<List<Cache>>

    @Query("DELETE FROM cache where `index` <> :keyIndex")
    fun clearAll(keyIndex: String)

    //Added coroutine functions
    @Query("SELECT * FROM cache WHERE `index` = :keyIndex")
    suspend fun getCacheData(keyIndex: String): Cache?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCache(cache: Cache)

    @Query("DELETE FROM cache WHERE `index` = :keyIndex")
    suspend fun deleteCache(keyIndex: String)
}