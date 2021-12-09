package com.example.weather.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM LastWeatherInfo")
    fun getAll(): Flow<MutableList<LastWeatherInfo>>

    @Query("SELECT * FROM LastWeatherInfo")
    fun getAllList(): List<LastWeatherInfo>

    @Insert(entity = LastWeatherInfo::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert(stopwatches: LastWeatherInfo?)

    @Query("SELECT * FROM DBForecast")
    fun getForecast(): Flow<List<DBForecast>>

    @Insert(entity = DBForecast::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert(stopwatches: DBForecast?)

    @Query("UPDATE LastWeatherInfo SET region=:region, speed=:speed, humidity=:humidity,pressure=:pressure, temperature=:temperature, lat=:lat, lon=:lon WHERE id=:id")
    fun update(
        region: String,
        speed: String,
        humidity: String,
        pressure: String,
        temperature: String,
        lat: String,
        lon: String,
        id: Int
    )

    @Update
    fun update(stopwatches: DBForecast?)
}