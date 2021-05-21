package com.example.weathertask.repository.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weathertask.model.Weather
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface WeatherDao {
    /**
     * this method will insert the data in the Room database
     * Important: in case that the data have the same id, it will replace the old one
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(weathers: List<Weather>)

    /**
     * this method will retrieve the data in the Room database
     * It will return only the data with the correct query
     */
    @Query("SELECT * FROM consolidate_weather WHERE `location` LIKE :location AND `date` LIKE :date")
    fun getAll(location: String, date: Date): Flow<List<Weather>>
}