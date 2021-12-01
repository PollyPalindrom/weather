package com.example.weather.domain.use_cases.get_weather_forecast_use_case

import com.example.weather.data.remote.forecast_entity.FullForecast
import com.example.weather.database.DBForecast
import com.example.weather.data.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ForecastUseCase @Inject constructor(private val repository: Repository) {

    suspend fun getForecast(lat: String, lon: String): FullForecast =
        repository.getForecast(lat, lon)

    fun getForecast(): Flow<List<DBForecast>> {
        return repository.getForecast()
    }

    fun insertForecast(list: List<DBForecast>) {
        repository.insertForecast(list)
    }

    fun checkConnection(): Boolean {
        return repository.checkConnection()
    }

    fun updateForecast(forecast: List<DBForecast>) {
        repository.updateForecast(forecast)
    }
}