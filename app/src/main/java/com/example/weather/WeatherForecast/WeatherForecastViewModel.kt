package com.example.weather.WeatherForecast

import android.location.Location
import android.location.LocationListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.ForecastEntity.*
import com.example.weather.recycler.RecyclerItem
import com.example.weather.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherForecastViewModel @Inject constructor(private val repository: Repository) :
    ViewModel(), LocationListener {

    var data = MutableStateFlow<List<RecyclerItem>>(emptyList())

    suspend fun getForecast(lan: String, lon: String): FullForecast =
        repository.getForecast(lan, lon)

    fun setData(lan: String, lon: String) {
        viewModelScope.launch(Dispatchers.IO) {
            data.value = formatForecast(getForecast(lan, lon).list)
        }
    }

    override fun onLocationChanged(p0: Location) {
        setData(p0.latitude.toString(), p0.longitude.toString())
    }

    override fun onProviderDisabled(provider: String) {}

    private fun formatForecast(list: List<Forecast>): MutableList<RecyclerItem> {
        var currentDate = list[0].dt_txt.substring(0, 10)
        val listFinal: MutableList<RecyclerItem> = mutableListOf()
        listFinal.add(RecyclerItem.Day(currentDate))
        list.forEach {
            if (currentDate == it.dt_txt.substring(0, 10)) {
                listFinal.add(
                    RecyclerItem.Forecast(
                        it.dt_txt,
                        it.weather[0].description,
                        it.weather[0].icon,
                        it.main.temp
                    )
                )
            } else {
                currentDate = it.dt_txt.substring(0, 10)
                listFinal.add(RecyclerItem.Day(currentDate))
                listFinal.add(
                    RecyclerItem.Forecast(
                        it.dt_txt,
                        it.weather[0].description,
                        it.weather[0].icon,
                        it.main.temp
                    )
                )
            }
        }
        return listFinal
    }
}