package com.example.weathertask.repository

import com.example.weathertask.MainCoroutineRule
import com.example.weathertask.getKnownCities
import com.example.weathertask.model.Resource
import com.example.weathertask.model.Weather
import com.example.weathertask.repository.api.WeatherRemoteData
import com.example.weathertask.repository.data.CityDao
import com.example.weathertask.repository.data.WeatherDao
import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryTest {
    private lateinit var sut: WeatherRepository

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var remoteData: WeatherRemoteData

    @Mock
    lateinit var dao: CityDao

    @Mock
    lateinit var weatherDao: WeatherDao

    lateinit var wearthers: List<Weather>

    @Before
    fun setup() {
        sut = WeatherRepository(remoteData, dao, weatherDao)
    }

    /**
     * The goal of this test is to make sure that singleSourceOfTruthStrategy is working
     * We should first emit the loading state
     * then get the data from the database should be empty
     * then ask the remote data
     * then if it s a success save it to the database
     * then ask again the latest value from the database => those value should be the same as the remote one
     */
    @Test
    fun `given a success fetch, getdata should provide the lastest values `() =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            wearthers = listOf(Weather())
            val date = Date()
            given(weatherDao.getAll(FAKE_STRING, date)).willReturn(flow { emit(listOf()) })
            given(remoteData.fetchData(FAKE_STRING, date)).willReturn(Resource.success(wearthers))
            given(weatherDao.insertAll(wearthers)).willAnswer {
                given(weatherDao.getAll(FAKE_STRING, date)).willReturn(flow { emit(wearthers) })
            }

            val result = sut.getWeathers(FAKE_STRING, date).toList()

            assert(result.first().status == Resource.Status.LOADING)
            assert(result[1].status == Resource.Status.SUCCESS)
            assert(result[1].data?.isEmpty() == true)
            assert(result[2].status == Resource.Status.SUCCESS)
            assert(result[2].data == wearthers)
        }

    @Test
    fun `given that the database has data, remoteData should not be called `() =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            wearthers = listOf(Weather())
            val date = Date()
            given(weatherDao.getAll(FAKE_STRING, date)).willReturn(flow { emit(wearthers) })

            val result = sut.getWeathers(FAKE_STRING, date).toList()

            assert(result.first().status == Resource.Status.LOADING)
            assert(result[1].status == Resource.Status.SUCCESS)
            assert(result[1].data == wearthers)
        }

    @Test
    fun `getCities should give the default cities `() =
        mainCoroutineRule.testDispatcher.runBlockingTest {

            val result = sut.getCities().first()

            assert(result == getKnownCities())
        }

    companion object {
        private const val FAKE_STRING = "FAKE_STRING"
    }
}