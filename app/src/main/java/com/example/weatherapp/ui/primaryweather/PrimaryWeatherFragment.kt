package com.example.weatherapp.ui.primaryweather

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentPrimaryWeatherBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrimaryWeatherFragment : Fragment(R.layout.fragment_primary_weather) {
    private var _binding: FragmentPrimaryWeatherBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPrimaryWeatherBinding.bind(view)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}