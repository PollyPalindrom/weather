package com.example.weather.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.weather.R
import com.example.weather.common.ConnectionManager
import com.example.weather.common.CurrentLocationManager
import com.example.weather.data.database.AppDatabase
import com.example.weather.data.remote.BoredApi
import com.example.weather.data.remote.WeatherApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class NotificationReceiver :
    BroadcastReceiver() {
    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var weatherApi: WeatherApi

    @Inject
    lateinit var boredApi: BoredApi

    @Inject
    lateinit var currentLocationManager: CurrentLocationManager

    @Inject
    lateinit var connectionManager: ConnectionManager

    override fun onReceive(p0: Context?, p1: Intent?) {
        if (connectionManager.checkForInternet()) {
            if (p0 != null) {
                getCurrentWeatherHere(p0)
            }
        } else {
            if (p0 != null) {
                createNotification(p0.getString(R.string.nice_day), p0)
            }
        }
    }

    private fun getCurrentWeatherHere(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            if (connectionManager.checkForInternet()) {
                val weather = database.weatherDao().getAllList()
                val lastLocationWeather =
                    if (weather.isNotEmpty()) weatherApi.getCurrentWeather(
                        weather[0].lat,
                        weather[0].lon
                    )
                    else null
                val activity = boredApi.getActivity()
                createNotification(
                    "Temperature:" + lastLocationWeather?.main?.temp + "°" + "\nMaybe it is a good day to:" + activity.activity,
                    context
                )
            }
        }
    }

    private fun createNotification(text: String, context: Context) {
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