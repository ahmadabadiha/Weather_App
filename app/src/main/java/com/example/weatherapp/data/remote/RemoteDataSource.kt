package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.CitiesItem
import retrofit2.Response
import retrofit2.http.Query

interface RemoteDataSource {
    suspend fun searchCity(query: String): Response<List<CitiesItem>>
}