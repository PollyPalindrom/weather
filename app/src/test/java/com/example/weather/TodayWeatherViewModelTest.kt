package com.example.weather

import com.example.weather.data.database.LastWeatherInfo
import com.example.weather.domain.use_cases.get_current_weather_use_case.*
import com.example.weather.presentation.current_weather.CurrentLocationListener
import com.example.weather.presentation.current_weather.CurrentWeatherState
import com.example.weather.presentation.current_weather.TodayWeatherViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test

class TodayWeatherViewModelTest {
    private val currentWeatherNetworkUseCase = mockk<CurrentWeatherNetworkUseCase>(relaxed = true)
    private val checkConnectionUseCase = mockk<CheckConnectionUseCase>(relaxed = true)
    private val getAllCurrentWeatherDatabaseUseCase =
        mockk<GetAllCurrentWeatherDatabaseUseCase>(relaxed = true)
    private val getAllFlowCurrentWeatherDatabaseUseCase =
        mockk<GetAllFlowCurrentWeatherDatabaseUseCase>(relaxed = true)
    private val insertCurrentWeatherDatabaseUseCase =
        mockk<InsertCurrentWeatherDatabaseUseCase>(relaxed = true)
    private val updateCurrentWeatherDatabaseUseCase =
        mockk<UpdateCurrentWeatherDatabaseUseCase>(relaxed = true)
    private val getLocationCurrentWeatherUseCase =
        mockk<GetLocationCurrentWeatherUseCase>(relaxed = true)
    private val weather = LastWeatherInfo("1", "2", "3", "4", "5", "6", "7")
    private val viewModel = TodayWeatherViewModel(
        currentWeatherNetworkUseCase,
        checkConnectionUseCase,
        getAllCurrentWeatherDatabaseUseCase,
        getAllFlowCurrentWeatherDatabaseUseCase,
        insertCurrentWeatherDatabaseUseCase,
        updateCurrentWeatherDatabaseUseCase,
        getLocationCurrentWeatherUseCase
    )
    private val listener = mockk<CurrentLocationListener>()

    @Before
    fun set() {
        every { getAllCurrentWeatherDatabaseUseCase.getAllList() } returns listOf(weather,weather)
    }

    @Test
    fun checkGetLocation() {
        viewModel.getLocation(listener)
        verify { getLocationCurrentWeatherUseCase.getLocation(listener) }
    }

    @Test
    fun checkConnection() {
        viewModel.check()
        verify { checkConnectionUseCase.checkConnection() }
    }

    @Test
    fun checkGetWeatherForecast() {
        MatcherAssert.assertThat(
            currentWeatherNetworkUseCase.invoke("20.0", "20.0"),
            CoreMatchers.instanceOf(Flow::class.java)
        )
    }

    @Test
    fun checkGetStoredWeather() {
        MatcherAssert.assertThat(
            viewModel.getStoredWeather(),
            CoreMatchers.instanceOf(Flow::class.java)
        )
    }

    @Test
    fun checkUpdateDatabaseNull() {
        viewModel.state.value = CurrentWeatherState(weather)
        viewModel.updateDatabase(null)
        verify {
            insertCurrentWeatherDatabaseUseCase.insert(weather)
        }
    }

    @Test
    fun checkUpdateDatabaseNotNull() {
        viewModel.state.value = CurrentWeatherState(weather)
        viewModel.updateDatabase(weather)
        verify {
            updateCurrentWeatherDatabaseUseCase.update(
                weather.region,
                weather.speed,
                weather.humidity,
                weather.pressure,
                weather.temperature,
                weather.lat,
                weather.lon,
                weather.id
            )
        }
    }
}