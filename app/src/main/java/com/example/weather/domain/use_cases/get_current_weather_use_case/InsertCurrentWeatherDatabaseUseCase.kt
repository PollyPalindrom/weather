package com.example.weather.domain.use_cases.get_current_weather_use_case

import com.example.weather.data.repository.Repository
import com.example.weather.data.database.LastWeatherInfo
import javax.inject.Inject

class InsertCurrentWeatherDatabaseUseCase @Inject constructor(private val repository: Repository) {

    fun insert(weather: LastWeatherInfo) {
        repository.insert(weather)
    }

}