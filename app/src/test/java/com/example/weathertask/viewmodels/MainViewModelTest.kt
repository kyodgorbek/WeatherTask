package com.example.weathertask.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weathertask.MainCoroutineRule
import com.example.weathertask.R
import com.example.weathertask.model.City
import com.example.weathertask.model.Resource
import com.example.weathertask.model.Weather
import com.example.weathertask.repository.WeatherRepository
import com.example.weathertask.viewmodels.MainViewModel.Companion.ERROR_500
import com.nhaarman.mockitokotlin2.given
import getOrAwaitValue
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

/**
 * I did not do all tests
 * But it s just a question of time
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest : TestCase() {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var sut: MainViewModel

    @Mock
    lateinit var repository: WeatherRepository
    lateinit var city: List<City>
    lateinit var weathers: List<Weather>

    @Before
    fun setup() {
        sut = MainViewModel(repository)
    }

    //region onSnackbarShown
    @Test
    fun `calling onSnackbarShown should set the _snackbar value to null`() {
        sut._snackbar.postValue(0)

        sut.onSnackbarShown()
        val value = sut.snackbar.getOrAwaitValue()

        assertNull(value)
    }
    //endregion

    //region getCities
    private fun fakeLoadingVenuesFlow() = flow {
        city = listOf(City())
        emit(city)
    }

    @Test
    fun `calling getCities should trigger cities livedata with the values`() =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            given(repository.getCities()).willReturn(fakeLoadingVenuesFlow())

            sut.getCities()
            val value = sut.cities.getOrAwaitValue()

            assertTrue(value == city)
        }
    //endregion

    //region isValid
    @Test
    fun `calling isValid when the data is valid should return true`() {
        sut.currentCity = City(woeId = "trtert")

        val result = sut.isValid()

        assertTrue(result)
    }

    @Test
    fun `calling isValid when the data is not valid should return false and trigger the snacbar`() {
        sut.currentCity = City(woeId = "")


        val result = sut.isValid()
        val value = sut.snackbar.getOrAwaitValue()

        assertTrue(value == R.string.select_city)
        assertTrue(result.not())
    }

    //endregion
    private fun createErrorFlow(msg: String): Flow<Resource<List<Weather>>> {
        return flow {
            weathers = listOf(Weather())
            val resource = Resource.error(msg, weathers)
            emit(resource)
        }
    }

    private fun testError(errorString: String, stringId: Int) =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            val date = Date()
            sut.currentCity = City(FAKE_QUERY)
            sut.currentDate = date
            given(repository.getWeathers(FAKE_QUERY, date)).willReturn(createErrorFlow(errorString))

            sut.getWeather()
            val spinnerValue = sut.spinner.getOrAwaitValue()
            val errorValue = sut.snackbar.getOrAwaitValue()

            assertTrue(spinnerValue.not())
            assertTrue(errorValue == stringId)
        }

    @Test
    fun `given that the repository will generate an error ERROR_500, calling searchVenue should only post false to the spinner and give the internal_error message`() {
        testError(ERROR_500, R.string.internal_error)
    }

    @Test
    fun `given that the repository will generate an error unknown_error, calling searchVenue should only post false to the spinner and give the unknown_error message`() {
        testError(FAKE_STRING, R.string.unknown_error)
    }

    //endregion
    companion object {
        private const val FAKE_QUERY = "FAKE_QUERY"
        private const val FAKE_STRING = "FAKE_STRING"
    }
}