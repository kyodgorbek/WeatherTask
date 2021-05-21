package com.example.weathertask.repository.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weathertask.model.Weather
import junit.framework.Assert
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {
    private lateinit var sut: WeatherDao
    private lateinit var db: MetaWeatherDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MetaWeatherDatabase::class.java
        ).build()
        sut = db.getWeatherDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun shouldretrieveOnlytheDataWithTheSameLocationAndDate() = runBlocking {
        val weathers = listOf(
            Weather("1", location = FAKE_NAME, date = date),
            Weather("2", location = "", date = date),
            Weather("3", location = FAKE_NAME, date = Date())
        )

        sut.insertAll(weathers)
        val result = sut.getAll(FAKE_NAME, date).first()

        Assert.assertTrue("", result.size == 1)
        Assert.assertTrue("", result[0] == weathers[0])
    }

    companion object {
        val date = Date()
        const val FAKE_NAME = "FAKE_NAME"
    }
}