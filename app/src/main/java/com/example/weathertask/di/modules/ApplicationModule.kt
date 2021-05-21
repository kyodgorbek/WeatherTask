package com.example.weathertask.di.modules

import android.content.Context
import com.example.weathertask.WeatherApplication
import com.example.weathertask.di.utils.AppContext

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {
    @AppContext
    @Provides
    @Singleton
    fun provideContext(application: WeatherApplication): Context = application
}