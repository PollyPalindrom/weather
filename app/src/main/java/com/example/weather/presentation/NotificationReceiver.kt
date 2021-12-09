package com.example.weather.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.example.weather.R
import com.example.weather.common.ConnectionManager
import com.example.weather.common.CurrentLocationManager
import com.example.weather.data.database.AppDatabase
import com.example.weather.data.database.WeatherDao
import com.example.weather.data.remote.BoredApi
import com.example.weather.data.remote.WeatherApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

private const val DATABASE_NAME = "LastWeatherInfo"

@AndroidEntryPoint
class NotificationReceiver :
    BroadcastReceiver() {

    private lateinit var weatherDao: WeatherDao

    private val weatherApi = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(WeatherApi::class.java)
    private val boredApi = Retrofit.Builder()
        .baseUrl("https://www.boredapi.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(BoredApi::class.java)

    lateinit var currentLocationManager: CurrentLocationManager

    lateinit var connectionManager: ConnectionManager

    private lateinit var context: Context

    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p0 != null) {
            context = p0
        }
        weatherDao = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .build().weatherDao()
        currentLocationManager = p0?.let { CurrentLocationManager(it) }!!
        connectionManager = ConnectionManager(p0)
        if (connectionManager.checkForInternet()) {
            getCurrentWeatherHere()
        } else {
            createNotification("Have a nice day!")
        }
    }

    private fun getCurrentWeatherHere() {
        CoroutineScope(Dispatchers.IO).launch {
            if (connectionManager.checkForInternet()) {
                val weather = weatherDao.getAllList()
                val lastLocationWeather =
                    weatherApi.getCurrentWeather(weather[0].lat, weather[0].lon)
                val activity = boredApi.getActivity()
                createNotification("Temperature:" + lastLocationWeather.main.temp + "°" + "\nMaybe it is a good day to:" + activity.activity)
            }
        }
    }

    private fun createNotification(text: String) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = "weatherApp"

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.day01)
            .setContentTitle("weather")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(text)
            )

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(Random.nextInt(), notificationBuilder.build())
    }
}