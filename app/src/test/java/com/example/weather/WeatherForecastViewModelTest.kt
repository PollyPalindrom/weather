package com.example.weather

import com.example.weather.data.database.DBForecast
import com.example.weather.domain.use_cases.get_current_weather_use_case.CheckConnectionUseCase
import com.example.weather.domain.use_cases.get_current_weather_use_case.GetAllCurrentWeatherDatabaseUseCase
import com.example.weather.domain.use_cases.get_weather_forecast_use_case.*
import com.example.weather.presentation.current_weather.CurrentLocationListener
import com.example.weather.presentation.weather_forecast.WeatherForecastState
import com.example.weather.presentation.weather_forecast.WeatherForecastViewModel
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class WeatherForecastViewModelTest {
    private val weatherForecastNetworkUseCase = mockk<WeatherForecastNetworkUseCase>(relaxed = true)
    private val getLocationWeatherForecastUseCase =
        mockk<GetLocationWeatherForecastUseCase>(relaxed = true)
    private val getFlowWeatherForecastDatabaseUseCase =
        mockk<GetFlowWeatherForecastDatabaseUseCase>(relaxed = true)
    private val insertWeatherForecastDatabaseUseCase =
        mockk<InsertWeatherForecastDatabaseUseCase>(relaxed = true)
    private val updateCurrentWeatherDatabaseUseCase =
        mockk<UpdateCurrentWeatherDatabaseUseCase>(relaxed = true)
    private val checkConnectionUseCase = mockk<CheckConnectionUseCase>(relaxed = true)
    private val getAllCurrentWeatherDatabaseUseCase = mockk<GetAllCurrentWeatherDatabaseUseCase>(relaxed = true)
    private val viewModel = WeatherForecastViewModel(
        weatherForecastNetworkUseCase,
        getLocationWeatherForecastUseCase,
        getFlowWeatherForecastDatabaseUseCase,
        insertWeatherForecastDatabaseUseCase,
        updateCurrentWeatherDatabaseUseCase,
        checkConnectionUseCase,
        getAllCurrentWeatherDatabaseUseCase
    )
    private val listener = mockk<CurrentLocationListener>()

    @Test
    fun checkGetLocation() {
        viewModel.getLocation(listener)
        verify { getLocationWeatherForecastUseCase.getLocation(listener) }
    }


    @Test
    fun checkGetWeatherForecast() {
        assertThat(
            weatherForecastNetworkUseCase.invoke("20.0", "20.0"),
            instanceOf(Flow::class.java)
        )
    }

    @Test
    fun checkSetDataNotNull() {
        val forecast = listOf(DBForecast("1", "2", "3", 17.4))
        viewModel.setData(forecast)
        verify {
            updateCurrentWeatherDatabaseUseCase.updateForecast(forecast)
        }
    }

    @Test
    fun checkSetDataNull() {
        val forecast: List<DBForecast>? = null
        val forecastToInsert = listOf(DBForecast("1", "2", "3", 17.4))
        viewModel.state.value = WeatherForecastState(
            forecastToInsert
        )
        viewModel.setData(forecast)
        verify {
            insertWeatherForecastDatabaseUseCase.insertForecast(forecastToInsert)
        }
    }


}