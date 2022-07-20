package com.example.weatherapp.data.repository

import com.example.weatherapp.data.remote.RemoteDataSource
import com.example.weatherapp.data.remote.safeApiCall
import retrofit2.http.Query
import javax.inject.Inject

class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {
    suspend fun searchCities(query: String) = safeApiCall {
        remoteDataSource.searchCity(query)
    }
}