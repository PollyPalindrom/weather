package com.example.weather.di

import android.content.Context
import androidx.room.Room
import com.example.weather.common.ConnectionManager
import com.example.weather.common.CurrentLocationManager
import com.example.weather.data.remote.BoredApi
import com.example.weather.data.remote.WeatherApi
import com.example.weather.data.remote.data_source.NetworkDataSource
import com.example.weather.data.repository.Repository
import com.example.weather.database.AppDatabase
import com.example.weather.database.DatabaseDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val DATABASE_NAME = "LastWeatherInfo"

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    @Singleton
    fun provideRepo(
        networkDataSource: NetworkDataSource,
        databaseDataSource: DatabaseDataSource,
        connectionManager: ConnectionManager,
        currentLocationManager: CurrentLocationManager
    ): Repository {
        return Repository(
            networkDataSource,
            databaseDataSource,
            connectionManager,
            currentLocationManager
        )
    }

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBoredApi(): BoredApi {
        return Retrofit.Builder()
            .baseUrl("https://www.boredapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(BoredApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .build()
    }
}