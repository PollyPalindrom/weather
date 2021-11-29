package com.example.weather.CurrentWeather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.database.LastWeatherInfo
import com.example.weather.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayWeatherViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    val current = getStoredWeather()
    val connection = MutableStateFlow(false)

    private fun getStoredWeather(): Flow<MutableList<LastWeatherInfo>> {
        return repository.getAll()
    }

    private suspend fun getCurrentWeather(
        lat: Double, lon: Double, address: String,
        isEmpty: Boolean
    ) {
        val weather = repository.getCurrentWeather(
            lat.toString(),
            lon.toString()
        )
        if (!isEmpty) {
            val lastWeather = current.toList()[0][0]
            lastWeather.apply {
                humidity = weather.main.humidity.toString()
                pressure = weather.main.pressure.toString()
                speed = weather.wind.speed.toString()
                region = address
            }
            repository.insert(lastWeather)
        } else {
            val lastWeather = LastWeatherInfo(
                address,
                weather.wind.speed.toString(),
                weather.main.humidity.toString(),
                weather.main.pressure.toString()
            )
            repository.insert(lastWeather)
        }
    }

    fun getData(lat: Double, lon: Double, address: String, isEmpty: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentWeather(
                lat, lon, address,
                isEmpty
            )
        }
    }

    fun check() {
        connection.value = repository.checkConnection()
        println(connection.value)
    }

}