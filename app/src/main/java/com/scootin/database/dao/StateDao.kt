package com.scootin.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scootin.database.table.State
import com.scootin.network.response.Address

@Dao
interface StateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(state: State)

    @Query("SELECT * FROM state WHERE `id` = :keyIndex")
    fun getState(keyIndex: String): LiveData<State>

    @Query("DELETE FROM state WHERE `id` = :keyIndex")
    fun delete(keyIndex: String)

    @Query("SELECT * FROM state WHERE 1")
    fun getAllState(): LiveData<List<State>>

    @Query("DELETE FROM state where `id` <> :keyIndex")
    fun clearAll(keyIndex: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertState(state: State)

    @Query("DELETE FROM state WHERE `id` = :keyIndex")
    suspend fun deleteState(keyIndex: String)
}