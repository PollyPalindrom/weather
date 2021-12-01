package com.example.weather.data.remote

import com.example.weather.data.remote.forecast_entity.FullForecast
import com.example.weather.data.remote.today_entity.CurrentWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("./forecast")
    suspend fun getWeatherForecastFiveDayThreeHour(
        @Query("lat") cityLatitude: String,
        @Query("lon") cityLongitude: String,
        @Query("appid") apiKey: String = "46fda51683a3924641702af5d926ca08",
        @Query("units") units: String = "metric"
    ): FullForecast

    @GET("./weather")
    suspend fun getCurrentWeather(
        @Query("lat") cityLatitude: String,
        @Query("lon") cityLongitude: String,
        @Query("appid") apiKey: String = "46fda51683a3924641702af5d926ca08",
        @Query("units") units: String = "metric",
    ): CurrentWeather
}