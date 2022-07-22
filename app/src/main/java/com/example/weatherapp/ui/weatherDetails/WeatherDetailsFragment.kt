package com.example.weatherapp.ui.weatherDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherDetailsBinding
import com.example.weatherapp.ui.convertToTempString
import com.example.weatherapp.ui.sharedviewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherDetailsFragment : Fragment(R.layout.fragment_weather_details) {
    private var _binding: FragmentWeatherDetailsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val args: WeatherDetailsFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWeatherDetailsBinding.bind(view)
        initSetViews()
    }

    private fun initSetViews() {
        binding.cityName.text = args.cityName
        binding.coordinates.text = "(latitude: " + args.lat + ", longitude: " + args.lon + ")"
        val forecast = sharedViewModel.forecast
        val temp = forecast.temp
        val feel = forecast.feels_like
        val date = java.text.SimpleDateFormat("EEEE dd MMMM yyyy")
        val receivedDate = java.util.Date(forecast.dt.toLong() * 1000)
        binding.date.text = date.format(receivedDate)
        val icon = forecast.weather[0].icon
        Glide.with(this).load("http://openweathermap.org/img/wn/$icon@2x.png").into(binding.weatherIcon)
        binding.minMaxWeather.text = convertToTempString(temp.max) + " / " + convertToTempString(temp.min)
        binding.morningTemp.text = convertToTempString(temp.morn)
        binding.morningFeel.text = "Feels like\n" + convertToTempString(feel.morn)
        binding.dayTemp.text = convertToTempString(temp.day)
        binding.dayFeel.text = "Feels like\n" + convertToTempString(feel.day)
        binding.eveningTemp.text = convertToTempString(temp.eve)
        binding.eveningFeel.text = "Feels like\n" + convertToTempString(feel.eve)
        binding.nightTemp.text = convertToTempString(temp.night)
        binding.nightFeel.text = "Feels like\n" + convertToTempString(feel.night)
        binding.sunrise.text = "Sunrise: " + convertToStringTime(forecast.sunrise)
        binding.sunset.text = "Sunset: " + convertToStringTime(forecast.sunset)
        binding.moonrise.text = "Moonrise: " + convertToStringTime(forecast.moonrise)
        binding.moonset.text = "Moonset: " + convertToStringTime(forecast.moonset)
        binding.humidity.text = forecast.humidity.toString() + "%"
        binding.wind.text = forecast.wind_speed.toString()
        binding.uv.text = forecast.uvi.toString()
    }

    private fun convertToStringTime(unixTime: Int): String {
        val timeFormat = java.text.SimpleDateFormat("HH:MM")
        val time = java.util.Date(unixTime.toLong() * 1000)
        return timeFormat.format(time)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}