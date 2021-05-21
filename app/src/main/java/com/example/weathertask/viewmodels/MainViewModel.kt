package com.example.weathertask.viewmodels

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathertask.R
import com.example.weathertask.model.City
import com.example.weathertask.model.CityUiModel
import com.example.weathertask.model.Resource
import com.example.weathertask.model.Weather
import com.example.weathertask.repository.WeatherRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor(var repository: WeatherRepository) : ViewModel() {
    @VisibleForTesting
    var weatherJob: Job? = null
    var currentCity: City = City()
    lateinit var currentDate: Date

    private val _weathers = MutableLiveData<List<Weather>?>()
    val weathers: LiveData<List<Weather>?>
        get() = _weathers

    private val _cities = MutableLiveData<List<City>?>()
    val cities: LiveData<List<City>?>
        get() = _cities

    /**
     * Show a loading spinner if true
     */
    private val _spinner = MutableLiveData<Boolean>(true)
    val spinner: LiveData<Boolean>
        get() = _spinner

    /**
     * Request a snackbar to display a message.
     * This is used to notify that we dont have internet for example
     */
    @VisibleForTesting
    val _snackbar = MutableLiveData<Int?>()
    val snackbar: LiveData<Int?>
        get() = _snackbar

    /**
     * We want to display the snackbar only once.
     * So after that the value is displayed, we should reset the value
     */
    fun onSnackbarShown() {
        _snackbar.value = null
    }

    fun getCities() {
        viewModelScope.launch {
            repository.getCities().collectLatest {
                _cities.postValue(it)
            }
        }
    }

    fun isValid() = when {
        currentCity.woeId.isEmpty() -> {
            _snackbar.postValue(R.string.select_city)
            false
        }
        else -> {
            true
        }
    }

    fun updateCitySelected(city: City): List<CityUiModel> {
        currentCity = city
        return cities.value?.map {
            CityUiModel(it, it.woeId == city.woeId)
        } ?: listOf()
    }

    fun setDate(time: Long) {
        Calendar.getInstance().apply {
            timeInMillis = time
            this[Calendar.HOUR_OF_DAY] = 0
            this[Calendar.MINUTE] = 0
            this[Calendar.SECOND] = 0
            this[Calendar.MILLISECOND] = 0
            currentDate = getTime()

        }
    }

    fun getWeather() {
        weatherJob?.cancel()
        val result = repository.getWeathers(currentCity.woeId, currentDate)
            .catch {
                weatherJob?.cancel()
            }
        weatherJob = viewModelScope.launch {
            result.collectLatest {
                handleResource(it)
            }
        }
    }

    private fun handleResource(result: Resource<List<Weather>>) {
        when (result.status) {
            Resource.Status.SUCCESS -> {
                _spinner.postValue(false)
                _weathers.postValue(result.data)
            }
            Resource.Status.LOADING -> {
                _spinner.postValue(true)
            }
            else -> {
                handleError(result.code)
            }
        }
    }

    /**
     * This method will have all error cases.
     * We will trigger the snackbar when we see the no internet error
     * Or we will post a value in _errorMessage with the correct message
     */
    private fun handleError(code: String?) {
        _spinner.postValue(false)
        _weathers.postValue(null)
        code?.contains(ERROR_NO_INTERNET)
        val msg = when (code) {
            ERROR_NO_INTERNET -> {
                R.string.wrong_location
            }
            ERROR_404 -> {
                R.string.wrong_location
            }
            ERROR_500 -> {
                R.string.internal_error
            }
            else -> {
                R.string.unknown_error
            }
        }
        _snackbar.postValue(msg)
    }

    companion object {
        const val ERROR_404 = "HTTP 404"
        const val ERROR_500 = "HTTP 500"
        const val ERROR_NO_INTERNET =
            "Unable to resolve host \"metaweather.com\": No address associated with hostname"
    }
}