package com.scootin.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scootin.database.table.EntityLocation

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: EntityLocation)

    @Query("SELECT * FROM entity_location")
    fun getCurrentLocation(): LiveData<EntityLocation?>

    @Query("SELECT * FROM entity_location")
    suspend fun getEntityLocation(): EntityLocation

    @Query("DELETE FROM entity_location")
    fun clearAll()
}