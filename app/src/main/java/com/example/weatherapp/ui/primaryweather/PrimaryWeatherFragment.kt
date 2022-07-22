package com.example.weatherapp.ui.primaryweather

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.remote.ResultWrapper
import com.example.weatherapp.databinding.FragmentPrimaryWeatherBinding
import com.example.weatherapp.ui.collectFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrimaryWeatherFragment : Fragment(R.layout.fragment_primary_weather) {
    private var _binding: FragmentPrimaryWeatherBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PrimaryWeatherViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPrimaryWeatherBinding.bind(view)
        binding.cityName.text = viewModel.cityName
        collectFlow(viewModel.weatherResults) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.isGone = false
                    binding.loadingAnim.playAnimation()
                }
                is ResultWrapper.Success -> {
                    binding.loadingAnim.isGone = true
                    binding.loadingAnim.pauseAnimation()
                    setViews(it.value)
                }
                is ResultWrapper.Error -> {
                    binding.loadingAnim.isGone = true
                    binding.loadingAnim.pauseAnimation()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setViews(weather: WeatherResponse) {
        binding.temp.text = weather.current.temp.toString() + "°"
        binding.feelsLike.text = "Feels like " + weather.current.feels_like.toString() + "°"
        val icon = weather.current.weather[0].icon
        Glide.with(this).load("http://openweathermap.org/img/wn/$icon@2x.png").into(binding.weatherIcon)
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
        val weekDay = java.text.SimpleDateFormat("EEEE")
        val date = java.util.Date(weather.current.dt.toLong() * 1000)
        binding.weekday.text = weekDay.format(date)
        binding.date.text = sdf.format(date)
        binding.weatherDescription.text = weather.current.weather[0].description
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}