package com.example.weatherapp.domain.UseCases

import com.example.weatherapp.data.model.CitiesItem
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.remote.ResultWrapper
import com.example.weatherapp.domain.repository.Repository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(lat: Float, lon: Float): kotlinx.coroutines.flow.Flow<ResultWrapper<WeatherResponse>> {
        return repository.getWeatherInfo(lat, lon)
    }
}