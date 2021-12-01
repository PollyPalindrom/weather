package com.example.weather.domain.use_cases.get_current_weather_use_case

import com.example.weather.data.repository.Repository
import com.example.weather.database.LastWeatherInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllFlowCurrentWeatherDatabaseUseCase @Inject constructor(private val repository: Repository) {

    fun getAll(): Flow<MutableList<LastWeatherInfo>> {
        return repository.getAll()
    }
}