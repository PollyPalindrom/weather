package com.example.weather

sealed class WeatherEvent {
    data class Share(var isClicked: Boolean) : WeatherEvent()
}