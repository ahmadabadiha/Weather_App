package com.example.weatherapp.ui.search

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.data.model.CitiesItem
import com.example.weatherapp.data.remote.ResultWrapper
import com.example.weatherapp.databinding.FragmentSearchBinding
import com.example.weatherapp.ui.collectFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchResultsRecyclerAdapter: SearchResultsRecyclerAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        initSetSearchViewListener()

        searchResultsRecyclerAdapter = SearchResultsRecyclerAdapter(::onCityItemClick)
        binding.recyclerView.adapter = searchResultsRecyclerAdapter

    }

    private fun initSetSearchViewListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank())
                    viewModel.searchCities(query)
                collectSearchResults()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return true
            }
        })
    }

    private fun collectSearchResults() {
        collectFlow(viewModel.searchResults) {
            when (it) {
                ResultWrapper.Loading -> {
                    binding.loadingAnim.isGone = false
                    binding.loadingAnim.playAnimation()
                    binding.welcomeGroup.isGone = true
                }
                is ResultWrapper.Success -> {
                    binding.loadingAnim.isGone = true
                    binding.loadingAnim.pauseAnimation()
                    searchResultsRecyclerAdapter.submitList(it.value)
                }
                is ResultWrapper.Error -> {
                    binding.loadingAnim.isGone = true
                    binding.loadingAnim.pauseAnimation()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun onCityItemClick(cityItem: CitiesItem) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToPrimaryWeatherFragment(
                cityItem.lat.toFloat(),
                cityItem.lon.toFloat(),
                cityItem.name
            )
        )
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}