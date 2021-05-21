package com.example.weathertask.repository.api

import com.example.weathertask.model.Resource
import com.example.weathertask.model.Weather
import com.nhaarman.mockitokotlin2.given
import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class WeatherRemoteDataTest {
    private lateinit var sut: WeatherRemoteData

    @Mock
    lateinit var venueApi: MetaWeatherApi

    @Before
    fun setup() {
        sut = WeatherRemoteData(venueApi)
    }

    //region getVenues
    @Test
    fun `given valid parameter to api, getWeather should return a success ressource with the data`() =
        runBlocking {
            val fakeResponse = listOf(Weather())
            val date = Date()
            val cal = Calendar.getInstance().apply { time = date }
            given(
                venueApi.getWeather(
                    FAKE_STRING,
                    cal.get(Calendar.YEAR).toString(),
                    cal.get(Calendar.MONTH).toString(),
                    cal.get(Calendar.DAY_OF_MONTH).toString()
                )
            ).willReturn(fakeResponse)

            val result = sut.fetchData(FAKE_STRING, date)

            Assert.assertTrue("should be SUCCESS", result.status == Resource.Status.SUCCESS)
            Assert.assertTrue("should have the same data", result.data == fakeResponse)
        }

    //region
    companion object {
        private const val FAKE_STRING = "FAKE_STRING"
    }
}