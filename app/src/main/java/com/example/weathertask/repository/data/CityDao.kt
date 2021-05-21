package com.example.weathertask.repository.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weathertask.model.City

/**
 * Note that this class is not used yet. But i wanted to show the possiblity to save also the city
 */
@Dao
interface CityDao {
    /**
     * this method will insert the data in the Room database
     * Important: in case that the data have the same id, it will replace the old one
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: City)

    @Query("SELECT * FROM city")
    fun getAll(): LiveData<List<City>>
}