package com.example.weathertask.model

/**
 * This class is used in order to add the value isSelected to an City without saving it in database
 */
data class CityUiModel(
    var city: City = City(),
    var isSelected: Boolean = false
)