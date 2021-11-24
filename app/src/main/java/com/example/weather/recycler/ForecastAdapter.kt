package com.example.weather.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.weather.R
import com.example.weather.databinding.DateItemBinding
import com.example.weather.databinding.ForecastItemBinding

class ForecastAdapter : ListAdapter<RecyclerItem, ForecastItemViewHolder>(ForecastDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastItemViewHolder {
        return when (viewType) {
            R.layout.date_item -> ForecastItemViewHolder.DayViewHolder(
                DateItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.forecast_item -> ForecastItemViewHolder.WeatherViewHolder(
                ForecastItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid ViewType Provided")
        }
    }

    override fun onBindViewHolder(holder: ForecastItemViewHolder, position: Int) {
        when (holder) {
            is ForecastItemViewHolder.DayViewHolder -> holder.bind(currentList[position] as RecyclerItem.Day)
            is ForecastItemViewHolder.WeatherViewHolder -> {
                val weather = currentList[position] as RecyclerItem.Forecast
                holder.bind(weather)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is RecyclerItem.Day -> R.layout.date_item
            is RecyclerItem.Forecast -> R.layout.forecast_item
        }
    }

}