package com.example.weatherapp.domain.UseCases

import com.example.weatherapp.data.model.CitiesItem
import com.example.weatherapp.data.remote.ResultWrapper
import com.example.weatherapp.domain.repository.Repository
import retrofit2.http.Query
import java.util.concurrent.Flow

class SearchCityUseCase(private val repository: Repository) {
    suspend operator fun invoke(query: String): kotlinx.coroutines.flow.Flow<ResultWrapper<List<CitiesItem>>> {
        return repository.searchCities(query)
    }
}