package com.example.weather.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApplication : Application() {
    companion object {
        lateinit var app: WeatherApplication
    }
}
