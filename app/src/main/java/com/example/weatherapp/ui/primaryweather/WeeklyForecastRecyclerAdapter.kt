package com.example.weatherapp.ui.primaryweather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.data.model.Daily
import com.example.weatherapp.databinding.WeekdayLayoutBinding


class WeeklyForecastRecyclerAdapter(private val onClick: (dailyForecast: Daily) -> Unit) :
    ListAdapter<Daily, WeeklyForecastRecyclerAdapter.WeekdayViewHolder>(
        WeeklyForecastDiffCallback()
    ) {

    inner class WeekdayViewHolder(private val binding: WeekdayLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun fill(item: Daily) {
            binding.apply {
                val icon = item.weather[0].icon
                Glide.with(itemView).load("http://openweathermap.org/img/wn/$icon@2x.png").into(weatherIcon)
                val dayOfMonth = java.text.SimpleDateFormat("dd MMM")
                val dayOfWeek = java.text.SimpleDateFormat("E")
                val receivedDate = java.util.Date(item.dt.toLong() * 1000)
                weekday.text = dayOfWeek.format(receivedDate)
                date.text = dayOfMonth.format(receivedDate)
                temp.text = item.temp.day.toString() + "°"
            }
            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekdayViewHolder =
        WeekdayViewHolder(
            WeekdayLayoutBinding.inflate(
                LayoutInflater
                    .from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: WeekdayViewHolder, position: Int) {
        holder.fill(getItem(position))
    }

}

class WeeklyForecastDiffCallback : DiffUtil.ItemCallback<Daily>() {
    override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem.dt == newItem.dt
    }

    override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem == newItem
    }
}