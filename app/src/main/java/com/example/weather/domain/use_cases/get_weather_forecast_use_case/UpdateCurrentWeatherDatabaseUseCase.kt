package com.example.weather.domain.use_cases.get_weather_forecast_use_case

import com.example.weather.data.repository.Repository
import com.example.weather.database.DBForecast
import javax.inject.Inject

class UpdateCurrentWeatherDatabaseUseCase @Inject constructor(private val repository: Repository) {

    fun updateForecast(forecast: List<DBForecast>) {
        repository.updateForecast(forecast)
    }

}