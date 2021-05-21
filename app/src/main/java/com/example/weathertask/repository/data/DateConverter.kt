package com.example.weathertask.repository.data

import androidx.room.TypeConverter
import java.util.*

/**
 * this class is needed by room in order to parse the Date
 */
class DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long): Date {
        return Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
}