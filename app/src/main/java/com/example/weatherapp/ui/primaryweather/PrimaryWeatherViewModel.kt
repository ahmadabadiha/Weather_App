package com.example.weatherapp.ui.primaryweather

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.remote.ResultWrapper
import com.example.weatherapp.domain.usecases.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrimaryWeatherViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    val lat get() = savedStateHandle.get<Float>("lat")!!
    val lon get() = savedStateHandle.get<Float>("lon")!!
    val cityName get() = savedStateHandle.get<String>("cityName")!!

    private val _weatherResults = MutableStateFlow<ResultWrapper<WeatherResponse>>(ResultWrapper.Loading)
    val weatherResults = _weatherResults.asStateFlow()

    init {
        getWeatherInfo()
    }

    private fun getWeatherInfo() = viewModelScope.launch(Dispatchers.IO) {
        getWeatherUseCase(lat, lon).collectLatest { _weatherResults.emit(it) }
    }

}