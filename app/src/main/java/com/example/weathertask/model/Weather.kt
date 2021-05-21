package com.example.weathertask.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * This class represent the data from the Api
 * I did map all but only a few is used
 */
@Entity(tableName = "consolidate_weather")
data class Weather(
    @PrimaryKey @field:SerializedName("id") val id: String = "",
    @field:SerializedName("weather_state_name") val stateName: String = "",
    @field:SerializedName("weather_state_abbr") val stateAbbr: String = "",
    @field:SerializedName("location") var location: String = "",
    @field:SerializedName("data") var date: Date = Calendar.getInstance().time,
    @field:SerializedName("wind_direction_compass") val directionCompass: String = "",
    @field:SerializedName("min_temp") val minTemp: Float = 0f,
    @field:SerializedName("max_temp") val maxTemp: Float = 0f,
    @field:SerializedName("wind_speed") val windSpeed: Float = 0f,
    @field:SerializedName("wind_direction") val windDirection: Float = 0f,
    @field:SerializedName("humidity") val humidity: Float = 0f,
    @field:SerializedName("predictability") val predictability: Float = 0f
)