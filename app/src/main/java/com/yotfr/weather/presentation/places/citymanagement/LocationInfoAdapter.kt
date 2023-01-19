package com.yotfr.weather.presentation.places.citymanagement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yotfr.weather.databinding.ItemLocationInfoBinding
import com.yotfr.weather.domain.places.model.LocationInfo

class LocationInfoDiffUtilCallback : DiffUtil.ItemCallback<LocationInfo>() {
    override fun areItemsTheSame(oldItem: LocationInfo, newItem: LocationInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LocationInfo, newItem: LocationInfo): Boolean {
        return oldItem == newItem
    }
}

class LocationInfoAdapter : ListAdapter<LocationInfo, LocationInfoAdapter.LocationInfoViewHolder>(
    LocationInfoDiffUtilCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationInfoViewHolder {
        return LocationInfoViewHolder(
            binding = ItemLocationInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LocationInfoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LocationInfoViewHolder(
        private val binding: ItemLocationInfoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(locationInfo: LocationInfo) {
            binding.apply {
                itemLocationInfoTvPlaceName.text = locationInfo.placeName
                itemLocationInfoTvCountryName.text = locationInfo.countryName
            }
        }
    }
}
