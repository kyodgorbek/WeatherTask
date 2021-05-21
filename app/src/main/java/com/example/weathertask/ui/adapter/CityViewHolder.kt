package com.example.weathertask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.weathertask.R

import com.example.weathertask.databinding.ListCitiesBinding
import com.example.weathertask.model.CityUiModel


class CityViewHolder(private val binding: ListCitiesBinding, var listener: CityListener) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(
            parent: ViewGroup, listener: CityListener
        ): CityViewHolder {
            val binding =
                ListCitiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CityViewHolder(binding, listener)
        }
    }

    fun bind(cityUiModel: CityUiModel) {
        with(binding) {
            cityContainer.setOnClickListener {
                listener.onClick(cityUiModel.city)
            }
            cityName.text = cityUiModel.city.name
            cityName.setBackgroundColor(
                ContextCompat.getColor(root.context, R.color.colorAccent).takeIf {
                    cityUiModel.isSelected
                } ?: ContextCompat.getColor(root.context, R.color.white)
            )
        }
    }
}