package com.example.weathertask.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weathertask.repository.data.MetaWeatherDatabase.Companion.CITY_TABLE
import com.google.gson.annotations.SerializedName

/**
 * This data class represent an City
 * WoeId is the unique identifier from the api
 */
@Entity(tableName = CITY_TABLE)
data class City(
    @PrimaryKey @field:SerializedName("woeid") val woeId: String = "",
    @field:SerializedName("name") val name: String = ""
)