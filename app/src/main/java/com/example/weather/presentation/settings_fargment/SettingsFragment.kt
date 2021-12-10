package com.example.weather.presentation.settings_fargment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.weather.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switch1: SwitchPreference? =
            findPreference(PREF_1) as SwitchPreference?
        switch1?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) {
                viewModel.setAlarm()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.notification_on),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                viewModel.cancelAlarm()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.notification_off),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            true
        }

        val switch2: SwitchPreference? =
            findPreference(PREF_2) as SwitchPreference?
        switch2?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.changeTheme(newValue as Boolean)
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
        private const val PREF_1 = "switch_preference_1"
        private const val PREF_2 = "switch_preference_2"
    }
}