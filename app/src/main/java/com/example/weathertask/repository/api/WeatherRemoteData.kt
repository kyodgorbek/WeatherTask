package com.example.weathertask.repository.api

import com.example.weathertask.model.Resource
import com.example.weathertask.model.Weather
import java.util.*
import javax.inject.Inject

/**
 * This class will use retrofit to call the api
 * It will not handle the error but give it to the ViewModel
 */
class WeatherRemoteData @Inject constructor(
    private val api: MetaWeatherApi
) {

    suspend fun fetchData(woeId: String, date: Date): Resource<List<Weather>> {
        return try {
            val cal = Calendar.getInstance().apply { time = date }
            val result = api.getWeather(
                woeId,
                cal.get(Calendar.YEAR).toString(),
                cal.get(Calendar.MONTH).toString(),
                cal.get(Calendar.DAY_OF_MONTH).toString()
            )
            result.forEach {
                it.date = date
                it.location = woeId
            }
            Resource.success(result)
        } catch (e: Exception) {
            Resource.error(e.message ?: "", emptyList())
        }
    }
}