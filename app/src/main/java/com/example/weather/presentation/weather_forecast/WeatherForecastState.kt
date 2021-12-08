package com.example.weather.presentation.weather_forecast

import com.example.weather.data.database.DBForecast

data class WeatherForecastState(
    val currentForecast: List<DBForecast> = emptyList(),
    val error: String = ""
)