package com.example.weather.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LastWeatherInfo::class, DBForecast::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}