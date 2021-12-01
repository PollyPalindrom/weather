package com.example.weather.domain.use_cases.get_weather_forecast_use_case

import com.example.weather.common.Resource
import com.example.weather.data.remote.forecast_entity.FullForecast
import com.example.weather.database.LastWeatherInfo
import com.example.weather.data.repository.Repository
import com.example.weather.data.remote.today_entity.CurrentWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherForecastNetworkUseCase @Inject constructor(private val repository: Repository) {

    operator fun invoke(lat: String, lon: String): Flow<Resource<FullForecast>> = flow {
        try {
            emit(Resource.Loading())
            val weather = repository.getForecast(lat, lon)
            emit(Resource.Success(weather))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "Couldn't reach server. Check your internet connection"
                )
            )
        }
    }
}