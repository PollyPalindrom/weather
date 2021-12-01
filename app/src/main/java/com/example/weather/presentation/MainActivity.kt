package com.example.weather.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weather.R
import com.example.weather.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private var activityResultLauncher: ActivityResultLauncher<String>

    init {
        this.activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                findNavController(R.id.my_host_activity).navigate(R.id.todayWeatherFragment)
                binding.menu.setupWithNavController(findNavController(R.id.my_host_activity))
            } else {
                findNavController(R.id.my_host_activity).navigate(R.id.errorFragment)
                Toast.makeText(this, "Can't get location without gps", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            findNavController(R.id.my_host_activity).navigate(R.id.todayWeatherFragment)
            binding.menu.setupWithNavController(findNavController(R.id.my_host_activity))
        }


    }
}
