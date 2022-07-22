package com.example.weatherapp.domain.usecases

import com.example.weatherapp.data.model.CitiesItem
import com.example.weatherapp.data.remote.ResultWrapper
import com.example.weatherapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow


class SearchCityUseCase(private val repository: Repository) {
    suspend operator fun invoke(query: String): Flow<ResultWrapper<List<CitiesItem>>> {
        return repository.searchCities(query)
    }
}