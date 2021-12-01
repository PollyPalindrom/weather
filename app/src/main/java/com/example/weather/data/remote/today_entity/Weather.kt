package com.example.weather.data.remote.today_entity

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)