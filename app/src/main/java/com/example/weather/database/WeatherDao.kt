package com.example.weather.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM LastWeatherInfo")
    fun getAll(): Flow<MutableList<LastWeatherInfo>>

    @Insert(entity = LastWeatherInfo::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert(stopwatches: LastWeatherInfo?)

    @Update
    fun update(stopwatches: LastWeatherInfo?)

    @Delete
    fun delete(stopwatches: LastWeatherInfo?)

    @Query("SELECT * FROM LastWeatherInfo WHERE id = :id")
    fun getLastWeatherInfo(id: Int): LastWeatherInfo
}