package com.example.weather.data.repository

import com.example.weather.common.ConnectionManager
import com.example.weather.common.CurrentLocationManager
import com.example.weather.data.remote.forecast_entity.FullForecast
import com.example.weather.data.database.DBForecast
import com.example.weather.data.database.DatabaseDataSource
import com.example.weather.data.database.LastWeatherInfo
import com.example.weather.data.remote.data_source.NetworkDataSource
import com.example.weather.data.remote.today_entity.CurrentWeather
import com.example.weather.presentation.current_weather.CurrentLocationListener
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val databaseDataSource: DatabaseDataSource,
    private val connectionManager: ConnectionManager,
    private val currentLocationManager: CurrentLocationManager
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
        databaseDataSource.update(
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
        databaseDataSource.updateForecast(forecast)
    }

    fun getAllList(): List<LastWeatherInfo> {
        return databaseDataSource.getAllList()
    }

    fun getLocation(listener: CurrentLocationListener) {
        currentLocationManager.getLocation(listener)
    }

}