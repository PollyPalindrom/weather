package com.example.weather.domain.use_cases.get_weather_forecast_use_case

import com.example.weather.data.repository.Repository
import com.example.weather.data.database.DBForecast
import javax.inject.Inject

class InsertWeatherForecastDatabaseUseCase @Inject constructor(private val repository: Repository) {

    fun insertForecast(list: List<DBForecast>) {
        repository.insertForecast(list)
    }

}