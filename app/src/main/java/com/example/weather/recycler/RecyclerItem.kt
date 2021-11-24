package com.example.weather.recycler

sealed class RecyclerItem {
    data class Day(
        val title: String
    ) : RecyclerItem()

    data class Forecast(
        var dt_txt: String,
        var description: String,
        var icon:String,
        var temperature:Double
    ) : RecyclerItem()
}