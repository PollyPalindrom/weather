package com.example.weather.WeatherForecast

import android.annotation.SuppressLint
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
import com.example.weather.ForecastParser
import com.example.weather.databinding.WeatherForecastFragmentBinding
import com.example.weather.recycler.ForecastAdapter
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherForecastFragment : Fragment() {

    private lateinit var binding: WeatherForecastFragmentBinding
    private val viewModel by viewModels<WeatherForecastViewModel>()
    private val forecastAdapter: ForecastAdapter = ForecastAdapter()
    private var isEmpty = true

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
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = forecastAdapter
        }
        lifecycleScope.launch {
            viewModel.currentForecast.collect {
                if (it.isNotEmpty()) {
                    forecastAdapter.submitList(forecastParser.formRecyclerItem(it))
                    isEmpty = false
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

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        LocationServices.getFusedLocationProviderClient(requireContext()).getCurrentLocation(
            LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener {
            viewModel.setData(it.latitude, it.longitude, isEmpty)
        }
    }
}