package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.remote.RemoteDataSource
import com.example.weatherapp.data.remote.safeApiCall
import javax.inject.Inject

class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {
    suspend fun searchCities(query: String) = safeApiCall {
        remoteDataSource.searchCity(query)
    }

    suspend fun getWeatherInfo(lat: Float, lon: Float) = safeApiCall {
        remoteDataSource.getWeatherInfo(lat, lon)
    }
}