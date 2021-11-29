package com.example.weather.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LastWeatherInfo::class, DBForecast::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}