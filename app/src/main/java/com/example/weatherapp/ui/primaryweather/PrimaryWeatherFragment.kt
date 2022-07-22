package com.example.weatherapp.ui.primaryweather

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Current
import com.example.weatherapp.data.remote.ResultWrapper
import com.example.weatherapp.databinding.FragmentPrimaryWeatherBinding
import com.example.weatherapp.ui.collectFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrimaryWeatherFragment : Fragment(R.layout.fragment_primary_weather) {
    private var _binding: FragmentPrimaryWeatherBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PrimaryWeatherViewModel by viewModels()
    private lateinit var weeklyForecastRecyclerAdapter: WeeklyForecastRecyclerAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPrimaryWeatherBinding.bind(view)
        binding.cityName.text = viewModel.cityName
        weeklyForecastRecyclerAdapter = WeeklyForecastRecyclerAdapter { }
        binding.recyclerView.adapter = weeklyForecastRecyclerAdapter
        collectFlow(viewModel.weatherResults) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.isGone = false
                    binding.loadingAnim.playAnimation()
                    binding.currentWeatherView.isGone = true
                }
                is ResultWrapper.Success -> {
                    binding.loadingAnim.isGone = true
                    binding.loadingAnim.pauseAnimation()
                    binding.currentWeatherView.isGone = false
                    setViews(it.value.current)
                    weeklyForecastRecyclerAdapter.submitList(it.value.daily)
                }
                is ResultWrapper.Error -> {
                    binding.loadingAnim.isGone = true
                    binding.loadingAnim.pauseAnimation()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setViews(currentWeather: Current) {
        binding.temp.text = currentWeather.temp.toString() + "°"
        binding.feelsLike.text = "Feels like " + currentWeather.feels_like.toString() + "°"
        val icon = currentWeather.weather[0].icon
        Glide.with(this).load("http://openweathermap.org/img/wn/$icon@2x.png").into(binding.weatherIcon)
        val date = java.text.SimpleDateFormat("dd MMMM yyyy")
        val time = java.text.SimpleDateFormat("HH:mm")
        val weekDay = java.text.SimpleDateFormat("E")
        val receivedDate = java.util.Date(currentWeather.dt.toLong() * 1000)
        binding.weekday.text = weekDay.format(receivedDate)
        binding.date.text = date.format(receivedDate)
        binding.time.text = time.format(receivedDate)
        binding.weatherDescription.text = currentWeather.weather[0].description
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}