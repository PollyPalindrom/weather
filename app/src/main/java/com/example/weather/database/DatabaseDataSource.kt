package com.example.weather.database

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatabaseDataSource @Inject constructor(@ApplicationContext private val context: Context) {
    private val database by lazy { AppDatabase.getInstance(context) }
    val weatherDao = database.weatherDao()

    fun getAll(): Flow<MutableList<LastWeatherInfo>> {
        return weatherDao.getAll()
    }

    fun insert(weather: LastWeatherInfo) {
        weatherDao.insert(weather)
    }

    fun delete(weather: LastWeatherInfo) {
        weatherDao.delete(weather)
    }

    fun update(weather: LastWeatherInfo) {
        weatherDao.update(weather)
    }

    fun getLastWeatherInfo(id: Int): LastWeatherInfo {
        return weatherDao.getLastWeatherInfo(id)
    }
}