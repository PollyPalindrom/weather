package com.example.weather.common

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.weather.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ThemeManager @Inject constructor(@ApplicationContext private val context: Context) {
    fun changeTheme(regime: Boolean) {
        if (regime) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            context.setTheme(R.style.DarkTheme)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            context.setTheme(R.style.AppTheme)
        }
    }
}