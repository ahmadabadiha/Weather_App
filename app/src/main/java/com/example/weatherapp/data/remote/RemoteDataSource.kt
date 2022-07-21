package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.CitiesItem
import com.example.weatherapp.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.Query

interface RemoteDataSource {
    suspend fun searchCity(query: String): Response<List<CitiesItem>>
    suspend fun getWeatherInfo(lat: Float, lon: Float): Response<WeatherResponse>
}