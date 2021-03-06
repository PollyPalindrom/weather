package com.example.weather.domain.use_cases.get_current_weather_use_case

import com.example.weather.data.repository.Repository
import javax.inject.Inject

class CheckConnectionUseCase @Inject constructor(private val repository: Repository) {

    fun checkConnection(): Boolean {
        return repository.checkConnection()
    }

}