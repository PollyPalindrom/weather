package com.example.weather.presentation.weather_forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.common.ForecastParser
import com.example.weather.common.Resource
import com.example.weather.data.database.DBForecast
import com.example.weather.domain.use_cases.get_current_weather_use_case.CheckConnectionUseCase
import com.example.weather.domain.use_cases.get_weather_forecast_use_case.*
import com.example.weather.presentation.current_weather.CurrentLocationListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherForecastViewModel @Inject constructor(
    private val weatherForecastNetworkUseCase: WeatherForecastNetworkUseCase,
    private val getLocationWeatherForecastUseCase: GetLocationWeatherForecastUseCase,
    private val getFlowWeatherForecastDatabaseUseCase: GetFlowWeatherForecastDatabaseUseCase,
    private val insertWeatherForecastDatabaseUseCase: InsertWeatherForecastDatabaseUseCase,
    private val updateCurrentWeatherDatabaseUseCase: UpdateCurrentWeatherDatabaseUseCase,
    private val checkConnectionUseCase: CheckConnectionUseCase
) :
    ViewModel() {

    var currentForecast = getFlowWeatherForecastDatabaseUseCase.getForecast()
    private val connection = MutableStateFlow(false)
    val state: MutableStateFlow<WeatherForecastState> = MutableStateFlow(WeatherForecastState())

    fun getWeatherForecast(
        lat: Double,
        lon: Double
    ) {
        weatherForecastNetworkUseCase.invoke(lat.toString(), lon.toString()).onEach { result ->
            val forecastParser = ForecastParser()
            when (result) {
                is Resource.Success -> {
                    state.value = WeatherForecastState(
                        forecastParser.formForecastForDB(result.data?.list)
                    )
                }
                is Resource.Error -> {
                    state.value = WeatherForecastState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setData(forecast: List<DBForecast>?) {
        viewModelScope.launch(Dispatchers.IO) {
            val forecastParser = ForecastParser()
            if (forecast == null) {
                insertWeatherForecastDatabaseUseCase.insertForecast(
                    state.value.currentForecast
                )
            } else {
                updateCurrentWeatherDatabaseUseCase.updateForecast(
                    forecastParser.updateForecast(
                        forecast,
                        state.value.currentForecast
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