package com.example.weather.WeatherForecast

import android.location.Location
import android.location.LocationListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.ForecastParser
import com.example.weather.repository.Repository
import com.example.weather.use_cases.ForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherForecastViewModel @Inject constructor(private val forecastUseCase: ForecastUseCase) :
    ViewModel(), LocationListener {

    var currentForecast = forecastUseCase.getForecast()
    val connection = MutableStateFlow(false)

    fun setData(lat: Double, lon: Double, isEmpty: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val weather =
                forecastUseCase.getForecast(lat.toString(), lon.toString())
            val forecastParser = ForecastParser()
            if (!isEmpty) {
                val lastWeather = currentForecast.toList()[0]
                forecastUseCase.insertForecast(
                    forecastParser.updateForecast(
                        lastWeather,
                        weather.list
                    )
                )
            } else {
                forecastUseCase.insertForecast(forecastParser.formForecastForDB(weather.list))
            }
        }
    }

    override fun onLocationChanged(p0: Location) {
        setData(p0.latitude, p0.longitude, false)
    }

    override fun onProviderDisabled(provider: String) {}

    fun check() {
        connection.value = forecastUseCase.checkConnection()
        println(connection.value)
    }
}