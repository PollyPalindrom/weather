package com.example.weather.presentation.settings_fargment

import androidx.lifecycle.ViewModel
import com.example.weather.common.AlarmManager
import com.example.weather.common.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themeManager: ThemeManager,
    private val alarmManager: AlarmManager
) : ViewModel() {
    fun changeTheme(regime: Boolean) {
        themeManager.changeTheme(regime)
    }

    fun setAlarm() {
        alarmManager.setAlarm()
    }

    fun cancelAlarm() {
        alarmManager.cancelAlarm()
    }
}