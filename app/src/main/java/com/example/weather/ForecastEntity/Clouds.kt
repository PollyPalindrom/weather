package com.example.weather.ForecastEntity

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all") var all: Int
)