package com.example.weather.repository

import com.example.weather.ConnectionManager
import com.example.weather.ForecastEntity.FullForecast
import com.example.weather.database.DBForecast
import com.example.weather.database.DatabaseDataSource
import com.example.weather.database.LastWeatherInfo
import com.example.weather.network.NetworkDataSource
import com.example.weather.todayEntity.CurrentWeather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val databaseDataSource: DatabaseDataSource,
    private val connectionManager: ConnectionManager
) {
    suspend fun getForecast(lat: String, lon: String): FullForecast =
        networkDataSource.getForecast(lat, lon)

    suspend fun getCurrentWeather(lat: String, lon: String): CurrentWeather =
        networkDataSource.getCurrentWeather(lat, lon)

    fun getAll(): Flow<MutableList<LastWeatherInfo>> {
        return databaseDataSource.getAll()
    }

    fun insert(weather: LastWeatherInfo) {
        databaseDataSource.insert(weather)
    }

    fun getForecast(): Flow<List<DBForecast>> {
        return databaseDataSource.getForecast()
    }

    fun insertForecast(list: List<DBForecast>) {
        databaseDataSource.insertForecast(list)
    }

    fun checkConnection(): Boolean {
        return connectionManager.checkForInternet()
    }

}