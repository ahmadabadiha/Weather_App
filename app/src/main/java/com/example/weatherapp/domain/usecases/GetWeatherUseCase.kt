package com.example.weatherapp.domain.usecases

import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.remote.ResultWrapper
import com.example.weatherapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(lat: Float, lon: Float): Flow<ResultWrapper<WeatherResponse>> {
        return repository.getWeatherInfo(lat, lon)
    }
}