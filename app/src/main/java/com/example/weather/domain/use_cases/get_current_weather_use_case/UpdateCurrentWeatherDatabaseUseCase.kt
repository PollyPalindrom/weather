package com.example.weather.domain.use_cases.get_current_weather_use_case

import com.example.weather.data.repository.Repository
import javax.inject.Inject

class UpdateCurrentWeatherDatabaseUseCase @Inject constructor(private val repository: Repository) {

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
        repository.update(
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

}