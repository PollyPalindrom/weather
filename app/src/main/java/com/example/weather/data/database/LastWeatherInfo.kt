package com.example.weather.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class LastWeatherInfo(
    var region: String,
    var speed: String,
    var humidity: String,
    var pressure: String,
    var temperature: String,
    var lat: String,
    var lon: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Entity
class DBForecast(
    var dt_txt: String,
    var description: String,
    var icon: String,
    var temperature: Double
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}