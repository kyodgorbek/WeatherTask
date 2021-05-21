package com.example.weathertask.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weathertask.model.Weather
import javax.inject.Inject

class WeatherAdapter @Inject constructor(comparator: WeatherComparator) :
    ListAdapter<Weather, RecyclerView.ViewHolder>(comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WeatherViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as WeatherViewHolder).bind(getItem(position))
    }
}