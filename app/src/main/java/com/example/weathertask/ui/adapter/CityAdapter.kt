package com.example.weathertask.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weathertask.model.City
import com.example.weathertask.model.CityUiModel
import javax.inject.Inject

/**
 * Listener when we click on a item from the list
 */
interface CityListener {
    fun onClick(city: City)
}

/**
 * We are using a ListAdapter.
 */
class CityAdapter @Inject constructor(cartItemsComparator: CityComparator) :
    ListAdapter<CityUiModel, RecyclerView.ViewHolder>(cartItemsComparator) {
    lateinit var listener: CityListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CityViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CityViewHolder).bind(getItem(position))
    }
}