package com.example.weather.common

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.widget.Toast
import com.example.weather.presentation.current_weather.CurrentLocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CurrentLocationManager @Inject constructor(@ApplicationContext private val context: Context) {
    @SuppressLint("MissingPermission")
    fun getLocation(listener: CurrentLocationListener, background: Boolean = false) {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && lm.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        )
            LocationServices.getFusedLocationProviderClient(context).getCurrentLocation(
                LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener {
                getAddress(it.latitude, it.longitude, listener)
            }
        else {
            if (!background) Toast.makeText(
                context,
                "There is no gps connection",
                Toast.LENGTH_SHORT
            ).show()
            listener.noGpsConnection()
        }
    }


    private fun getAddress(lat: Double, lon: Double, listener: CurrentLocationListener) {
        val geocoder = Geocoder(context)
        val list = geocoder.getFromLocation(lat, lon, 1)
        listener.getCurrentWeatherHere(lat, lon, list[0].locality + ", " + list[0].countryName)
    }
}