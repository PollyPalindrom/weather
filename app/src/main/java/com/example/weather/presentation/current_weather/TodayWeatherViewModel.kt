package com.example.weather.presentation.current_weather

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.WeatherEvent
import com.example.weather.common.Resource
import com.example.weather.database.LastWeatherInfo
import com.example.weather.domain.use_cases.get_current_weather_use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayWeatherViewModel @Inject constructor(
    private val currentWeatherNetworkUseCase: CurrentWeatherNetworkUseCase,
    private val checkConnectionUseCase: CheckConnectionUseCase,
    private val getAllCurrentWeatherDatabaseUseCase: GetAllCurrentWeatherDatabaseUseCase,
    private val getAllFlowCurrentWeatherDatabaseUseCase: GetAllFlowCurrentWeatherDatabaseUseCase,
    private val insertCurrentWeatherDatabaseUseCase: InsertCurrentWeatherDatabaseUseCase,
    private val updateCurrentWeatherDatabaseUseCase: UpdateCurrentWeatherDatabaseUseCase,
    private val getLocationCurrentWeatherUseCase: GetLocationCurrentWeatherUseCase
) :
    ViewModel() {
    val state: MutableStateFlow<CurrentWeatherState?> = MutableStateFlow(null)
    val current = getStoredWeather()
    val connection = MutableStateFlow(false)
    val intent: MutableStateFlow<Intent?> = MutableStateFlow(null)
    var weatherShare: LastWeatherInfo? = null

    fun getCurrentWeather(
        lat: Double,
        lon: Double,
        region: String,
        weather: LastWeatherInfo?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            currentWeatherNetworkUseCase.invoke(lat.toString(), lon.toString()).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        state.value = CurrentWeatherState(
                            LastWeatherInfo(
                                region,
                                result.data?.wind?.speed.toString(),
                                result.data?.main?.humidity.toString(),
                                result.data?.main?.pressure.toString(),
                                result.data?.main?.temp.toString()
                            )
                        )
                    }
                    is Resource.Error -> {
                        state.value = CurrentWeatherState(
                            error = result.message ?: "An unexpected error occurred"
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun getStoredWeather(): Flow<MutableList<LastWeatherInfo>> {
        return getAllFlowCurrentWeatherDatabaseUseCase.getAll()
    }

    fun onEvent(event: WeatherEvent) {
        when (event) {
            is WeatherEvent.Share -> {
                println("Start share")
                createIntent()
            }
        }
    }

    fun updateDatabase(weather: LastWeatherInfo?) {
        viewModelScope.launch(Dispatchers.IO) {
            val lastWeather = state.value?.currentWeather
            if (weather == null) {
                if (lastWeather != null) {
                    insertCurrentWeatherDatabaseUseCase.insert(lastWeather)
                }

            } else {
                val databaseWeather = getAllCurrentWeatherDatabaseUseCase.getAllList()[0]
                databaseWeather.apply {
                    speed = lastWeather?.speed.toString()
                    pressure = lastWeather?.pressure.toString()
                    humidity = lastWeather?.humidity.toString()
                    region = lastWeather?.region.toString()
                    temperature = lastWeather?.temperature.toString()
                }
                updateCurrentWeatherDatabaseUseCase.update(
                    databaseWeather.region,
                    databaseWeather.speed,
                    databaseWeather.humidity,
                    databaseWeather.pressure,
                    databaseWeather.temperature,
                    databaseWeather.id
                )
            }
            weatherShare = lastWeather
        }
    }

    fun check() {
        connection.value = checkConnectionUseCase.checkConnection()
    }

    fun getLocation(listener: CurrentLocationListener) {
        getLocationCurrentWeatherUseCase.getLocation(listener)
    }

    private fun createTextForShare(): String {
        return "Place: " + weatherShare?.region + ";\n Temperature: " + weatherShare?.temperature
    }

    private fun createIntent() {
        val text = createTextForShare()
        println("The text is: " + text)
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT, text
            )
            type = "text/plain"
        }
        intent.value = Intent.createChooser(sendIntent, null)
    }

}