package com.example.weathertask.repository

import com.example.weathertask.getKnownCities
import com.example.weathertask.model.City
import com.example.weathertask.model.Resource
import com.example.weathertask.model.Weather
import com.example.weathertask.repository.api.WeatherRemoteData
import com.example.weathertask.repository.data.CityDao
import com.example.weathertask.repository.data.WeatherDao

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val remoteData: WeatherRemoteData,
    private val cityDao: CityDao,
    private val dao: WeatherDao
) {
    /**
     * An improvement could be to retrieve the data from the cityDao
     * And add an functionality to add them in the database
     */
    suspend fun getCities(): Flow<List<City>> = flow {
        // in the future we could used the cityDao in order to retrieve the data from DB
        // for now i will use the default values
        emit(getKnownCities())
    }

    fun getWeathers(location: String, date: Date): Flow<Resource<List<Weather>>> {
        return resourceAsFlow(
            fetchFromLocal = { dao.getAll(location, date) },
            networkCall = { remoteData.fetchData(location, date) },
            saveCallResource = { weathers -> dao.insertAll(weathers) })
    }

}