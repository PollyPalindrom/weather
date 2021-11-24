package com.example.weather.CurrentWeather

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.weather.databinding.TodayWeatherFragmentBinding
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodayWeatherFragment : Fragment(), LocationListener {

    private lateinit var binding: TodayWeatherFragmentBinding

    private val viewModel by viewModels<TodayWeatherViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TodayWeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLocation()
        lifecycleScope.launch {
            viewModel.address.collect {
                binding.region.text = it
            }
        }
        lifecycleScope.launch {
            viewModel.current.collect {
                binding.humidityValue.text = it.main.humidity.toString()
                binding.pressureValue.text = it.main.pressure.toString()
                binding.windValue.text = it.wind.speed.toString()
            }
        }
    }

    override fun onLocationChanged(p0: Location) {
        getAddress(p0.latitude, p0.longitude)
    }

    private fun getLocation() {
        if ((ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                2
            )
        }
        LocationServices.getFusedLocationProviderClient(requireContext()).getCurrentLocation(
            LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener {
            getAddress(it.latitude, it.longitude)
        }
    }

    override fun onProviderDisabled(provider: String) {}

    private fun getAddress(lat: Double, lng: Double) {
        val geocoder = Geocoder(requireContext())
        val list = geocoder.getFromLocation(lat, lng, 1)
        viewModel.setAddress(list[0].locality + ", " + list[0].countryName)
        viewModel.getData(list[0].latitude.toString(), list[0].longitude.toString())
    }
}