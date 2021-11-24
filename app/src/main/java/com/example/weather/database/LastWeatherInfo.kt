package com.example.weather.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weather.recycler.RecyclerItem

@Entity
class LastWeatherInfo(
    var region: String,
    var speed: String,
    var humidity: String,
    var pressure: String,
    var forecast: List<RecyclerItem>
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}