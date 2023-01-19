package com.yotfr.weather.presentation.favoriteplaces

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yotfr.weather.databinding.ItemFavoritePlaceInfoBinding
import com.yotfr.weather.domain.model.PlaceInfo

class FavoritePlacesDiffUtilCallback : DiffUtil.ItemCallback<PlaceInfo>() {
    override fun areItemsTheSame(oldItem: PlaceInfo, newItem: PlaceInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PlaceInfo, newItem: PlaceInfo): Boolean {
        return oldItem == newItem
    }
}

class FavoritePlacesAdapter : ListAdapter<PlaceInfo, FavoritePlacesAdapter.FavoritePlaceViewHolder>(
    FavoritePlacesDiffUtilCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePlaceViewHolder {
        return FavoritePlaceViewHolder(
            binding = ItemFavoritePlaceInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoritePlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FavoritePlaceViewHolder(
        private val binding: ItemFavoritePlaceInfoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(placeInfo: PlaceInfo) {
            binding.apply {
                itemFavoritePlaceInfoTvPlaceName.text = placeInfo.placeName
                itemFavoritePlaceInfoTvCountryName.text = placeInfo.countryName
            }
        }
    }
}