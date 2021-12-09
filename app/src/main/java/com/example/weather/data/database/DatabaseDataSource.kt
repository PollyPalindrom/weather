package com.example.weather.data.database

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DatabaseDataSource @Inject constructor(private val database: AppDatabase) {
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

    fun update(
        region: String,
        speed: String,
        humidity: String,
        pressure: String,
        temperature: String,
        lat: String,
        lon: String,
        id: Int
    ) {
        weatherDao.update(
            region,
            speed,
            humidity,
            pressure,
            temperature,
            lat,
            lon,
            id
        )
    }

    fun updateForecast(forecast: List<DBForecast>) {
        forecast.forEach {
            weatherDao.update(it)
        }
    }

    fun getAllList(): List<LastWeatherInfo> {
        return weatherDao.getAllList()
    }
}