package com.example.weather.ForecastEntity

import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("clouds")var clouds: Clouds,
    @SerializedName("dt")var dt: Int,
    @SerializedName("dt_txt")var dt_txt: String,
    @SerializedName("main")var main: Main,
    @SerializedName("rain")var rain: Rain,
    @SerializedName("sys")var sys: Sys,
    @SerializedName("weather")var weather: List<Weather>,
    @SerializedName("wind")var wind: Wind
)