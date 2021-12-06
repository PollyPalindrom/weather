package com.example.weather.presentation.current_weather

interface CurrentLocationListener {
    fun getCurrentWeatherHere(lat: Double, lon: Double, region: String)
    fun noGpsConnection()
}