package com.example.weathertask.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.weathertask.model.CityUiModel

/**
 * We are comparing with the woeId as it s the primary key
 */
class CityComparator : DiffUtil.ItemCallback<CityUiModel>() {
    override fun areItemsTheSame(oldItem: CityUiModel, newItem: CityUiModel): Boolean {
        return oldItem.city.woeId == newItem.city.woeId
    }

    override fun areContentsTheSame(oldItem: CityUiModel, newItem: CityUiModel): Boolean {
        return oldItem == newItem
    }
}