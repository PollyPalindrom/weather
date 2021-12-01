package com.example.weather.data.remote.forecast_entity

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all") var all: Int
)