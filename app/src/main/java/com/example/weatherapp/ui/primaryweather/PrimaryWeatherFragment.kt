package com.example.weatherapp.ui.primaryweather

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Current
import com.example.weatherapp.data.model.Daily
import com.example.weatherapp.data.remote.ResultWrapper
import com.example.weatherapp.databinding.FragmentPrimaryWeatherBinding
import com.example.weatherapp.ui.collectFlow
import com.example.weatherapp.ui.convertToTempString
import com.example.weatherapp.ui.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrimaryWeatherFragment : Fragment(R.layout.fragment_primary_weather) {
    private var _binding: FragmentPrimaryWeatherBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PrimaryWeatherViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var weeklyForecastRecyclerAdapter: WeeklyForecastRecyclerAdapter
    private lateinit var weeklyForecast: List<Daily>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPrimaryWeatherBinding.bind(view)
        initSetViewsDetails()
        initCollectFlowResults()
        initSetOnClickListeners()
    }

    private fun initSetOnClickListeners() {
        binding.currentWeatherView.setOnClickListener {
            sharedViewModel.forecast = weeklyForecast[0]
            findNavController().navigate(
                PrimaryWeatherFragmentDirections.actionPrimaryWeatherFragmentToWeatherDetailsFragment(
                    viewModel.lat,
                    viewModel.lon,
                    viewModel.cityName
                )
            )
        }
    }

    private fun initSetViewsDetails() {
        binding.cityName.text = viewModel.cityName
        weeklyForecastRecyclerAdapter = WeeklyForecastRecyclerAdapter(::doOnWeeklyForecastItemClick)
        binding.recyclerView.adapter = weeklyForecastRecyclerAdapter
    }


    private fun initCollectFlowResults() {
        collectFlow(viewModel.weatherResults) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.isGone = false
                    binding.loadingAnim.playAnimation()
                    binding.loadedResultsGroup.isGone = true
                }
                is ResultWrapper.Success -> {
                    binding.loadingAnim.isGone = true
                    binding.loadingAnim.pauseAnimation()
                    binding.loadedResultsGroup.isGone = false
                    setViews(it.value.current)
                    weeklyForecast = it.value.daily
                    weeklyForecastRecyclerAdapter.submitList(weeklyForecast)
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
        binding.temp.text = convertToTempString(currentWeather.temp)
        binding.feelsLike.text = "Feels like " + convertToTempString(currentWeather.temp)
        val icon = currentWeather.weather[0].icon
        Glide.with(this).load("http://openweathermap.org/img/wn/$icon@2x.png").into(binding.weatherIcon)
        val date = java.text.SimpleDateFormat("dd MMMM yyyy")
        val time = java.text.SimpleDateFormat("HH:mm")
        val weekDay = java.text.SimpleDateFormat("EEEE")
        val receivedDate = java.util.Date(currentWeather.dt.toLong() * 1000)
        binding.weekday.text = weekDay.format(receivedDate)
        binding.date.text = date.format(receivedDate)
        binding.time.text = time.format(receivedDate)
        binding.weatherDescription.text = currentWeather.weather[0].description
    }

    private fun doOnWeeklyForecastItemClick(index: Int) {
        sharedViewModel.forecast = weeklyForecast[index]
        findNavController().navigate(
            PrimaryWeatherFragmentDirections.actionPrimaryWeatherFragmentToWeatherDetailsFragment(
                viewModel.lat,
                viewModel.lon,
                viewModel.cityName
            )
        )
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}