package com.example.weathertask

import android.content.ContentValues
import com.example.weathertask.model.City

fun getKnownCities(): List<City> {
    val london = City("44418", "London")
    val gothenburg = City("890869", "Gothenburg")
    val stockholm = City("906057", "Stockholm")
    val mountainiew = City("2455920", "Mountain View")
    val newYork = City("2459115", "New York")
    val berlin = City("638242", "Berlin")
    return listOf(london, gothenburg, stockholm, mountainiew, newYork, berlin)
}

fun City.toContentValues(): ContentValues? {
    ContentValues().apply {
        put("id", this@toContentValues.woeId)
        put("name", this@toContentValues.name)
        return this
    }
}

fun String.createUrl(): String {
    return "https://www.metaweather.com/static/img/weather/png/64/$this.png"
}

fun String.displayAs(template: String): String {
    return "$template : $this"
}

fun Float.displayAs(template: String): String {
    return "$template : $this"
}