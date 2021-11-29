package com.example.weather.use_cases

import com.example.weather.ForecastEntity.FullForecast
import com.example.weather.database.DBForecast
import com.example.weather.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ForecastUseCase @Inject constructor(private val repository: Repository) {

    suspend fun getForecast(lat: String, lon: String): FullForecast =
        repository.getForecast(lat, lon)

    fun getForecast(): Flow<List<DBForecast>> {
        return repository.getForecast()
    }

    fun insertForecast(list: List<DBForecast>) {
        repository.insertForecast(list)
    }

    fun checkConnection(): Boolean {
        return repository.checkConnection()
    }
}