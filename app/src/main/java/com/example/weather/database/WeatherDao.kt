package com.example.weather.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM LastWeatherInfo")
    fun getAll(): Flow<MutableList<LastWeatherInfo>>

    @Insert(entity = LastWeatherInfo::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert(stopwatches: LastWeatherInfo?)

    @Query("SELECT * FROM DBForecast")
    fun getForecast(): Flow<List<DBForecast>>

    @Insert(entity = DBForecast::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert(stopwatches: DBForecast?)
}