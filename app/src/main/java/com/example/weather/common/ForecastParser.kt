package com.example.weather.common

import com.example.weather.data.remote.forecast_entity.Forecast
import com.example.weather.data.database.DBForecast
import com.example.weather.presentation.recycler.RecyclerItem

class ForecastParser {
    fun formRecyclerItem(list: List<DBForecast>): MutableList<RecyclerItem> {
        var currentDate = list[0].dt_txt.substring(0, 10)
        val listFinal: MutableList<RecyclerItem> = mutableListOf()
        listFinal.add(RecyclerItem.Day(currentDate))
        list.forEach {
            if (currentDate == it.dt_txt.substring(0, 10)) {
                listFinal.add(
                    RecyclerItem.Forecast(
                        it.dt_txt,
                        it.description,
                        it.icon,
                        it.temperature
                    )
                )
            } else {
                currentDate = it.dt_txt.substring(0, 10)
                listFinal.add(RecyclerItem.Day(currentDate))
                listFinal.add(
                    RecyclerItem.Forecast(
                        it.dt_txt,
                        it.description,
                        it.icon,
                        it.temperature
                    )
                )
            }
        }
        return listFinal
    }

    fun formForecastForDB(list: List<Forecast>?): MutableList<DBForecast> {
        val listFinal: MutableList<DBForecast> = mutableListOf()
        list?.forEach {
            listFinal.add(
                DBForecast(
                    it.dt_txt,
                    it.weather[0].description,
                    it.weather[0].icon,
                    it.main.temp
                )
            )
        }
        return listFinal
    }

    fun updateForecast(list: List<DBForecast>, newList: List<DBForecast>): List<DBForecast> {
        list.forEachIndexed { index1, dbForecast ->
            newList.forEachIndexed { index2, forecast ->
                if (index1 == index2) {
                    dbForecast.description = forecast.description
                    dbForecast.dt_txt = forecast.dt_txt
                    dbForecast.icon = forecast.icon
                    dbForecast.temperature = forecast.temperature
                }
            }
        }
        return list
    }
}