package com.example.weathertask.repository.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.weathertask.getKnownCities
import com.example.weathertask.model.City
import com.example.weathertask.model.Weather

import com.example.weathertask.toContentValues


@Database(
    entities = [City::class, Weather::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class MetaWeatherDatabase : RoomDatabase() {
    abstract fun getCityDao(): CityDao
    abstract fun getWeatherDao(): WeatherDao

    companion object {
        private const val DB_NAME = "MetaWeather.db"
        const val CITY_TABLE = "city"

        @Volatile
        private var INSTANCE: MetaWeatherDatabase? = null

        fun getInstance(context: Context): MetaWeatherDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun prepopulateDatabase() = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                getKnownCities().forEach {
                    db.insert("city", OnConflictStrategy.REPLACE, it?.toContentValues())
                }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MetaWeatherDatabase::class.java, DB_NAME
            ).build()
    }
}