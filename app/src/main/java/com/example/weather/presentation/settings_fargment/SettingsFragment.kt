package com.example.weather.presentation.settings_fargment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.weather.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switch1: SwitchPreference? =
            findPreference(PREF_1) as SwitchPreference?
        switch1?.setOnPreferenceChangeListener { _, newValue ->
//            viewModel.changeState(newValue as Boolean)
            true
        }

        val switch2: SwitchPreference? =
            findPreference(PREF_2) as SwitchPreference?
        switch2?.setOnPreferenceChangeListener { _, newValue ->
//            viewModel.changeState(newValue as Boolean)
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
}