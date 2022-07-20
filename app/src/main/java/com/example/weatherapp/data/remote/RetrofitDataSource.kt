package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.CitiesItem
import retrofit2.Response
import javax.inject.Inject

class RetrofitDataSource @Inject constructor(private val weatherService: WeatherService) : RemoteDataSource {
    override suspend fun searchCity(query: String): Response<List<CitiesItem>> {
        return weatherService.searchCity(query)
    }
}