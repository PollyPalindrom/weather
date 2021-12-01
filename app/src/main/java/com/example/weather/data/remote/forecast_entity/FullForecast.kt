package com.example.weather.data.remote.forecast_entity

import com.google.gson.annotations.SerializedName

data class FullForecast(
    @SerializedName("cod") var cod: String,
    @SerializedName("message") var message: Double,
    @SerializedName("cnt") var cnt: Int,
    @SerializedName("list") var list: List<Forecast>
)