package com.example.weather.presentation.current_weather

import com.example.weather.database.LastWeatherInfo

data class CurrentWeatherState(val currentWeather: LastWeatherInfo? = null, val error: String = "")