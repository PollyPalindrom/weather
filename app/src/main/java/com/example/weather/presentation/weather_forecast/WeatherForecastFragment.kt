package com.example.weather.presentation.weather_forecast

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.common.CurrentLocationManager
import com.example.weather.common.ForecastParser
import com.example.weather.database.DBForecast
import com.example.weather.databinding.WeatherForecastFragmentBinding
import com.example.weather.presentation.current_weather.CurrentLocationListener
import com.example.weather.recycler.ForecastAdapter
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherForecastFragment : Fragment(), CurrentLocationListener {

    private lateinit var binding: WeatherForecastFragmentBinding
    private val viewModel by viewModels<WeatherForecastViewModel>()
    private val forecastAdapter: ForecastAdapter = ForecastAdapter()
    private var forecast: List<DBForecast>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WeatherForecastFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val forecastParser = ForecastParser()
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
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = forecastAdapter
        }
        lifecycleScope.launch {
            viewModel.currentForecast.collect {
                if (it.isNotEmpty()) {
                    forecastAdapter.submitList(forecastParser.formRecyclerItem(it))
                    forecast = it
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

    fun getLocation() {
        viewModel.getLocation(this)
    }

    override fun getCurrentWeatherHere(lat: Double, lon: Double, region: String) {
        viewModel.setData(lat, lon, forecast)
    }
}