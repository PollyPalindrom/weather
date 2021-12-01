package com.example.weather.data.remote.data_source

import com.example.weather.data.remote.WeatherApi
import com.example.weather.data.remote.forecast_entity.FullForecast
import com.example.weather.data.remote.today_entity.CurrentWeather
import javax.inject.Inject

class NetworkDataSource @Inject constructor(private val weatherApi: WeatherApi) {

    suspend fun getForecast(lat: String, lon: String): FullForecast =
        weatherApi.getWeatherForecastFiveDayThreeHour(lat, lon)

    suspend fun getCurrentWeather(lat: String, lon: String): CurrentWeather =
        weatherApi.getCurrentWeather(lat, lon)
}