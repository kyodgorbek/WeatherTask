package com.example.weathertask.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.weathertask.model.Weather

class WeatherComparator : DiffUtil.ItemCallback<Weather>() {
    override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean {
        return oldItem == newItem
    }
}