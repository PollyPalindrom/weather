package com.example.weather.database

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val DATABASE_NAME = "LastWeatherInfo"

class DatabaseDataSource @Inject constructor(@ApplicationContext context: Context) {
    private val database by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .build()
    }
    private val weatherDao = database.weatherDao()

    fun getAll(): Flow<MutableList<LastWeatherInfo>> {
        return weatherDao.getAll()
    }

    fun insert(weather: LastWeatherInfo) {
        weatherDao.insert(weather)
    }

    fun getForecast(): Flow<List<DBForecast>> {
        return weatherDao.getForecast()
    }

    fun insertForecast(forecast: List<DBForecast>) {
        forecast.forEach {
            weatherDao.insert(it)
        }
    }
}