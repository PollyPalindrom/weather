package com.example.weather.recycler

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.weather.R
import com.example.weather.databinding.DateItemBinding
import com.example.weather.databinding.ForecastItemBinding
import kotlin.math.roundToInt

sealed class ForecastItemViewHolder(
    private val binding: ViewBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    class DayViewHolder(private val binding: DateItemBinding) : ForecastItemViewHolder(binding) {
        fun bind(day: RecyclerItem.Day) {
            binding.date.text = day.title;
        }
    }

    class WeatherViewHolder(private val binding: ForecastItemBinding) :
        ForecastItemViewHolder(binding) {
        fun bind(weather: RecyclerItem.Forecast) {
            binding.apply {
                temperature.text = weather.temperature.roundToInt().toString() + "°"
                weather.dt_txt.forEachIndexed { index, c ->
                    if (c == ' ') {
                        time.text = weather.dt_txt.substring(index + 1, index + 6)
                    }
                }
                description.text = weather.description
                image.setImageResource(
                    when (weather.icon) {
                        "01d" -> R.drawable.day01
                        "02d" -> R.drawable.day02
                        "03d" -> R.drawable.day_night_03_04
                        "04d" -> R.drawable.day_night_03_04
                        "09d" -> R.drawable.day_night_09
                        "10d" -> R.drawable.day10
                        "11d" -> R.drawable.day_night_11
                        "13d" -> R.drawable.day_night_13
                        "50d" -> R.drawable.day_night_50
                        "01n" -> R.drawable.night01
                        "02n" -> R.drawable.night02
                        "03n" -> R.drawable.day_night_03_04
                        "04n" -> R.drawable.day_night_03_04
                        "09n" -> R.drawable.day_night_09
                        "10n" -> R.drawable.night10
                        "11n" -> R.drawable.day_night_11
                        "13n" -> R.drawable.day_night_13
                        "50n" -> R.drawable.day_night_50
                        else -> R.drawable.day01
                    }
                )
            }
        }
    }

}