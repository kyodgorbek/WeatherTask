package com.example.weathertask.di

import com.example.weathertask.WeatherApplication
import com.example.weathertask.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        ApplicationModule::class,
        ActivityModule::class,
        ViewModelModule::class,
        AdapterModule::class,
        RepositoryModule::class,
        AndroidSupportInjectionModule::class
    ]
)
@Singleton
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: WeatherApplication): Builder

        fun build(): AppComponent
    }

    fun inject(application: WeatherApplication)
}