package com.example.weather.presentation.settings_fargment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.weather.R
import com.example.weather.presentation.NotificationReceiver
import java.util.*


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switch1: SwitchPreference? =
            findPreference("switch_preference_1") as SwitchPreference?
        switch1?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) {
                setAlarm()
                Toast.makeText(requireContext(), "Notifications are on now", Toast.LENGTH_SHORT)
                    .show()
                println("Yup!!!!!!")
            } else {
                cancelAlarm()
                Toast.makeText(requireContext(), "Notifications are off now", Toast.LENGTH_SHORT)
                    .show()
            }
            true
        }

        val switch2: SwitchPreference? =
            findPreference("switch_preference_2") as SwitchPreference?
        switch2?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                requireActivity().setTheme(R.style.DarkTheme)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                requireActivity().setTheme(R.style.AppTheme)
            }
            true
        }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference_fragment)
    }

    companion object {
        private const val PREF_1 = "PREF_1"
        private const val PREF_2 = "PREF_2"
    }

    private fun cancelAlarm() {
        val alarmManager =
            requireContext().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
        alarmManager.cancel(pendingIntent)
    }

    private fun setAlarm() {
        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val alarmManager =
            requireContext().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

    }
}