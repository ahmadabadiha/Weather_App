package com.example.weatherapp.data.remote

import android.provider.UserDictionary.Words.APP_ID
import com.example.weatherapp.data.model.CitiesItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("http://api.openweathermap.org/geo/1.0/direct")
    suspend fun searchCity(
        @Query("q") query: String,
        @Query("limit") limit: Int = 10,
        @Query("appid") appId: String = API_KEY
    ): Response<List<CitiesItem>>
}