package com.example.weather.domain.use_cases.get_current_weather_use_case

import com.example.weather.data.repository.Repository
import com.example.weather.data.database.LastWeatherInfo
import javax.inject.Inject

class GetAllCurrentWeatherDatabaseUseCase @Inject constructor(private val repository: Repository) {

    fun getAllList(): List<LastWeatherInfo> {
        return repository.getAllList()
    }
}