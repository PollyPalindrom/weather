package com.example.weather.presentation.weather_forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.common.ForecastParser
import com.example.weather.database.DBForecast
import com.example.weather.domain.use_cases.get_current_weather_use_case.CheckConnectionUseCase
import com.example.weather.domain.use_cases.get_weather_forecast_use_case.*
import com.example.weather.presentation.current_weather.CurrentLocationListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherForecastViewModel @Inject constructor(
    private val forecastUseCase: ForecastUseCase,
    private val getLocationWeatherForecastUseCase: GetLocationWeatherForecastUseCase,
    private val getFlowWeatherForecastDatabaseUseCase: GetFlowWeatherForecastDatabaseUseCase,
    private val insertWeatherForecastDatabaseUseCase: InsertWeatherForecastDatabaseUseCase,
    private val updateCurrentWeatherDatabaseUseCase: UpdateCurrentWeatherDatabaseUseCase,
    private val checkConnectionUseCase: CheckConnectionUseCase
) :
    ViewModel() {

    var currentForecast = getFlowWeatherForecastDatabaseUseCase.getForecast()
    val connection = MutableStateFlow(false)

    fun setData(lat: Double, lon: Double, forecast: List<DBForecast>?) {
        viewModelScope.launch(Dispatchers.IO) {
            val weather =
                forecastUseCase.getForecast(lat.toString(), lon.toString())
            val forecastParser = ForecastParser()
            if (forecast == null) {
                insertWeatherForecastDatabaseUseCase.insertForecast(
                    forecastParser.formForecastForDB(
                        weather.list
                    )
                )
            } else {
                updateCurrentWeatherDatabaseUseCase.updateForecast(
                    forecastParser.updateForecast(
                        forecast,
                        weather.list
                    )
                )
            }
        }
    }

    fun check() {
        connection.value = checkConnectionUseCase.checkConnection()
    }

    fun getLocation(listener: CurrentLocationListener) {
        getLocationWeatherForecastUseCase.getLocation(listener)
    }
}