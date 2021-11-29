package com.example.weather.use_cases

import com.example.weather.database.LastWeatherInfo
import com.example.weather.repository.Repository
import com.example.weather.todayEntity.CurrentWeather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrentWeatherUseCase @Inject constructor(private val repository: Repository) {
    suspend fun getCurrentWeather(lat: String, lon: String): CurrentWeather =
        repository.getCurrentWeather(lat, lon)

    fun getAll(): Flow<MutableList<LastWeatherInfo>> {
        return repository.getAll()
    }

    fun insert(weather: LastWeatherInfo) {
        repository.insert(weather)
    }

    fun checkConnection(): Boolean {
        return repository.checkConnection()
    }
}