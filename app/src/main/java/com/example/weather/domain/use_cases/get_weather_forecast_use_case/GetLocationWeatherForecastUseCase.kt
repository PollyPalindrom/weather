package com.example.weather.domain.use_cases.get_weather_forecast_use_case

import com.example.weather.data.repository.Repository
import com.example.weather.presentation.current_weather.CurrentLocationListener
import javax.inject.Inject

class GetLocationWeatherForecastUseCase @Inject constructor(private val repository: Repository) {

    fun getLocation(listener: CurrentLocationListener) {
        repository.getLocation(listener)
    }
}