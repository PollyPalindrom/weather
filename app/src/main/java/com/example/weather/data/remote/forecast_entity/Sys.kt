package com.example.weather.data.remote.forecast_entity

import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("pod")var pod: String
)