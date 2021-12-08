package com.example.weather

import com.example.weather.common.AlarmManager
import com.example.weather.common.ThemeManager
import com.example.weather.presentation.settings_fargment.SettingsViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class SettingsViewModelTest {
    private val themeManager = mockk<ThemeManager>(relaxed = true)
    private val alarmManager = mockk<AlarmManager>(relaxed = true)
    private val viewModel = SettingsViewModel(themeManager, alarmManager)

    @Test
    fun checkThemeChange() {
        viewModel.changeTheme(true)
        verify { themeManager.changeTheme(true) }
    }

    @Test
    fun checkSetAlarm() {
        viewModel.setAlarm()
        verify { alarmManager.setAlarm() }
    }

    @Test
    fun checkCancelAlarm() {
        viewModel.cancelAlarm()
        verify { alarmManager.cancelAlarm() }
    }
}