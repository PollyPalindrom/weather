package com.example.weather.domain.use_cases.get_weather_forecast_use_case

import com.example.weather.data.repository.Repository
import com.example.weather.database.DBForecast
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFlowWeatherForecastDatabaseUseCase @Inject constructor(private val repository: Repository) {

    fun getForecast(): Flow<List<DBForecast>> {
        return repository.getForecast()
    }
}