package com.example.weather.network

import com.example.weather.ForecastEntity.FullForecast
import com.example.weather.todayEntity.CurrentWeather
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class NetworkDataSource @Inject constructor() {
    private val weatherApi = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(WeatherApi::class.java)

    suspend fun getForecast(lat: String, lon: String): FullForecast =
        weatherApi.getWeatherForecastFiveDayThreeHour(lat, lon)

    suspend fun getCurrentWeather(lat: String, lon: String): CurrentWeather =
        weatherApi.getCurrentWeather(lat, lon)
}