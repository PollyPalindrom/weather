package com.example.weather.CurrentWeather

import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
    private var isEmpty = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TodayWeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println(1)
        viewModel.check()
        lifecycleScope.launch {
            viewModel.connection.collect {
                if (!it) {
                    Toast.makeText(
                        requireContext(),
                        "There is no internet connection",
                        Toast.LENGTH_LONG
                    ).show()
                } else getLocation()
            }
        }
        println(2)
        lifecycleScope.launch {
            viewModel.current.collect {
                if (it.isNotEmpty()) {
                    binding.humidityValue.text = it[0].humidity
                    binding.pressureValue.text = it[0].pressure
                    binding.windValue.text = it[0].speed
                    binding.region.text = it[0].region
                    isEmpty = false
                    println(it.size)
                    println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

    override fun onLocationChanged(p0: Location) {
        getAddress(p0.latitude, p0.longitude)
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        LocationServices.getFusedLocationProviderClient(context).getCurrentLocation(
            LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener {
            println("????????????????????????????????????")
            getAddress(it.latitude, it.longitude)
        }
    }


    fun getAddress(lat: Double, lon: Double) {
        println("1111111111111111111111111111111111111111111111111111")
        val geocoder = Geocoder(context)
        val list = geocoder.getFromLocation(lat, lon, 1)
        viewModel.getData(
            lat, lon, list[0].locality + ", " + list[0].countryName,
            isEmpty
        )
    }

    override fun onProviderDisabled(provider: String) {}
}