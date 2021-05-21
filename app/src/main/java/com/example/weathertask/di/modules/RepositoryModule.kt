package com.example.weathertask.di.modules

import android.content.Context
import com.example.weathertask.di.utils.API
import com.example.weathertask.di.utils.AppContext
import com.example.weathertask.repository.WeatherRepository
import com.example.weathertask.repository.api.MetaWeatherApi
import com.example.weathertask.repository.api.WeatherRemoteData
import com.example.weathertask.repository.data.CityDao
import com.example.weathertask.repository.data.MetaWeatherDatabase
import com.example.weathertask.repository.data.WeatherDao
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideDb(@AppContext context: Context) = MetaWeatherDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideVenueRepository(
        remoteData: WeatherRemoteData,
        cityDao: CityDao,
        dao: WeatherDao
    ): WeatherRepository =
        WeatherRepository(remoteData, cityDao, dao)

    @Singleton
    @Provides
    fun provideCityDao(db: MetaWeatherDatabase) = db.getCityDao()

    @Singleton
    @Provides
    fun provideWeatherDao(db: MetaWeatherDatabase) = db.getWeatherDao()

    @Singleton
    @Provides
    fun provideVenueRemoteData(api: MetaWeatherApi) = WeatherRemoteData(api)

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideAuthInterceptor() = HttpLoggingInterceptor()

    @API
    @Provides
    @Singleton
    fun provideOkhttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(httpLoggingInterceptor)
        return httpClient.build()
    }


    @Singleton
    @Provides
    fun provideWeatherApi(
        @API okhttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) = createRetrofit(okhttpClient, gsonConverterFactory).create(MetaWeatherApi::class.java)

    private fun createRetrofit(
        okhttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MetaWeatherApi.BASE_URL)
            .client(okhttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

}