package com.example.weather.CurrentWeather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.database.LastWeatherInfo
import com.example.weather.repository.Repository
import com.example.weather.use_cases.CurrentWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayWeatherViewModel @Inject constructor(private val currentWeatherUseCase: CurrentWeatherUseCase) :
    ViewModel() {
    val current = getStoredWeather()
    val connection = MutableStateFlow(false)

    private fun getStoredWeather(): Flow<MutableList<LastWeatherInfo>> {
        return currentWeatherUseCase.getAll()
    }

    private suspend fun getCurrentWeather(
        lat: Double, lon: Double, address: String,
        isEmpty: Boolean
    ) {
        val weather = currentWeatherUseCase.getCurrentWeather(
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
            currentWeatherUseCase.insert(lastWeather)
        } else {
            val lastWeather = LastWeatherInfo(
                address,
                weather.wind.speed.toString(),
                weather.main.humidity.toString(),
                weather.main.pressure.toString()
            )
            currentWeatherUseCase.insert(lastWeather)
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
        connection.value = currentWeatherUseCase.checkConnection()
        println(connection.value)
    }

}