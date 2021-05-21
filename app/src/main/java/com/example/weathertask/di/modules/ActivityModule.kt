package com.example.weathertask.di.modules

import com.example.weathertask.ui.CityDetailsFragment
import com.example.weathertask.ui.HomeFragment
import com.example.weathertask.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector()
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector()
    abstract fun contributeCityWeatherFragment(): CityDetailsFragment
}