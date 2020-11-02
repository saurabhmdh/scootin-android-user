package com.scootin.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scootin.network.response.Address

@Dao
interface AddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(address: Address)

    @Query("SELECT * FROM address WHERE `id` = :keyIndex")
    fun getAddress(keyIndex: String): LiveData<Address>

    @Query("DELETE FROM address WHERE `id` = :keyIndex")
    fun delete(keyIndex: String)

    @Query("SELECT * FROM address WHERE 1")
    fun getAllAddress(): LiveData<List<Address>>

    @Query("DELETE FROM address where `id` <> :keyIndex")
    fun clearAll(keyIndex: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: Address)

    @Query("DELETE FROM cache WHERE `index` = :keyIndex")
    suspend fun deleteAddress(keyIndex: String)
}