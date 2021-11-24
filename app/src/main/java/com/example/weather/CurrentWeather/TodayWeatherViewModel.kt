package com.example.weather.CurrentWeather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.repository.Repository
import com.example.weather.todayEntity.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayWeatherViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {
    val address = MutableStateFlow<String>("")
    val current = MutableStateFlow<CurrentWeather>(
        CurrentWeather(
            "",
            Clouds(0),
            0,
            Coord(0.0, 0.0),
            0,
            0,
            Main(0.0, 0, 0, 0.0, 0.0, 0.0),
            "",
            Sys("", 0, 0.0, 0, 0, 0),
            0,
            emptyList(),
            Wind(0.0, 0.0)
        )
    )

    fun setAddress(newAddress: String) {
        address.value = newAddress
    }

    suspend fun getCurrentWeather(lat: String, lon: String) {
        current.value = repository.getCurrentWeather(lat, lon)
        println(current.value.main.pressure)
    }

    fun getData(lat: String, lon: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentWeather(lat, lon)
        }
    }

}