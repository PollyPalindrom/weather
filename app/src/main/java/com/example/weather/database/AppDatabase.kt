package com.example.weather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DATABASE_NAME = "LastWeatherInfo"


@Database(entities = [LastWeatherInfo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao


    companion object {

        private var database: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (database == null) {
                database =
                    Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                        .build()
            }
            return database!!
        }
    }

}