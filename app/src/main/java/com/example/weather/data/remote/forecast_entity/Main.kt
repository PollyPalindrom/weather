package com.example.weather.data.remote.forecast_entity

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("grnd_level")var grnd_level: Double,
    @SerializedName("humidity")var humidity: Int,
    @SerializedName("pressure")var pressure: Double,
    @SerializedName("sea_level")var sea_level: Double,
    @SerializedName("temp")var temp: Double,
    @SerializedName("temp_kf")var temp_kf: Double,
    @SerializedName("temp_max")var temp_max: Double,
    @SerializedName("temp_min")var temp_min: Double
)