package com.example.weather.presentation.current_weather

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import com.example.weather.WeatherEvent
import com.example.weather.database.LastWeatherInfo
import com.example.weather.databinding.TodayWeatherFragmentBinding
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TodayWeatherFragment : Fragment(), CurrentLocationListener {

    private lateinit var binding: TodayWeatherFragmentBinding
    private val viewModel by viewModels<TodayWeatherViewModel>()
    private var weather: LastWeatherInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TodayWeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.check()
        (requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).registerDefaultNetworkCallback(
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    getLocation()
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    Toast.makeText(
                        requireContext(),
                        "There is no internet connection",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.whenStarted {
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
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.whenStarted {
                viewModel.current.collect {
                    if (it.isNotEmpty()) {
                        binding.humidityValue.text = it[0].humidity
                        binding.pressureValue.text = it[0].pressure
                        binding.windValue.text = it[0].speed
                        binding.region.text = it[0].region
                        binding.temperatureValue.text = it[0].temperature
                        weather = it[0]
                        println(it.size)
                    }
                }
            }
        }
        binding.share.setOnClickListener {
            viewModel.onEvent(WeatherEvent.Share(true))
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.whenStarted {
                viewModel.intent.collect {
                    if (it != null) {
                        startActivity(it)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.whenStarted {
                viewModel.state.collect {
                    if (it?.currentWeather != null) {
                        viewModel.updateDatabase(weather)
                    }
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

    override fun getCurrentWeatherHere(lat: Double, lon: Double, region: String) {
        println("OOOOOkay")
        println(region + "!!!!!!!!!!!!!!!!!")
        viewModel.getCurrentWeather(lat, lon, region, weather)
    }

    private fun getLocation() {
        viewModel.getLocation(this)
    }
}