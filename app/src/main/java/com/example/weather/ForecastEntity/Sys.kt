package com.example.weather.ForecastEntity

import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("pod")var pod: String
)