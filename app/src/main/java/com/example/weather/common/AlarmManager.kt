package com.example.weather.common

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.presentation.NotificationReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class AlarmManager @Inject constructor(@ApplicationContext private val context: Context) {
    fun setAlarm() {
        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val alarmManager =
            context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelAlarm() {
        val alarmManager =
            context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.cancel(pendingIntent)
    }
}