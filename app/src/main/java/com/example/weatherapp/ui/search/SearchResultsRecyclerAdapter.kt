package com.example.weatherapp.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.model.CitiesItem
import com.example.weatherapp.databinding.SearchResultBinding

class SearchResultsRecyclerAdapter(private val onClick: (product: CitiesItem) -> Unit) :
    ListAdapter<CitiesItem, SearchResultsRecyclerAdapter.ResultViewHolder>(
        SearchResultDiffCallback()
    ) {

    inner class ResultViewHolder(private val binding: SearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun fill(item: CitiesItem) {
            binding.apply {
                cityTV.text = item.name
                if (item.state != null)
                    stateTV.text = item.state + ", " + item.country
                else
                    stateTV.text = item.country
            }
            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder = ResultViewHolder(
        SearchResultBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.fill(getItem(position))
    }

}

class SearchResultDiffCallback : DiffUtil.ItemCallback<CitiesItem>() {
    override fun areItemsTheSame(oldItem: CitiesItem, newItem: CitiesItem): Boolean {
        return oldItem.lat == newItem.lat && oldItem.lon == newItem.lon
    }

    override fun areContentsTheSame(oldItem: CitiesItem, newItem: CitiesItem): Boolean {
        return oldItem == newItem
    }
}