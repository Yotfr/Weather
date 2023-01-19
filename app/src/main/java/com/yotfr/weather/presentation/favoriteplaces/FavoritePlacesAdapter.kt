package com.yotfr.weather.presentation.favoriteplaces

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yotfr.weather.databinding.ItemFavoritePlaceInfoBinding
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.model.PlaceInfo

class FavoritePlacesDiffUtilCallback : DiffUtil.ItemCallback<FavoritePlaceInfo>() {
    override fun areItemsTheSame(oldItem: FavoritePlaceInfo, newItem: FavoritePlaceInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FavoritePlaceInfo, newItem: FavoritePlaceInfo): Boolean {
        return oldItem == newItem
    }
}

class FavoritePlacesAdapter : ListAdapter<FavoritePlaceInfo, FavoritePlacesAdapter.FavoritePlaceViewHolder>(
    FavoritePlacesDiffUtilCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePlaceViewHolder {
        return FavoritePlaceViewHolder(
            binding = ItemFavoritePlaceInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            context = parent.context
        )
    }

    override fun onBindViewHolder(holder: FavoritePlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FavoritePlaceViewHolder(
        private val binding: ItemFavoritePlaceInfoBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(placeInfo: FavoritePlaceInfo) {
            binding.apply {
                itemFavoritePlaceInfoTvPlaceName.text = placeInfo.placeName
                itemFavoritePlaceInfoTvCountryName.text = placeInfo.countryName
                itemFavoritePlaceInfoIvWeatherType.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        placeInfo.weatherCode.iconRes
                    )
                )
                itemFavoritePlaceInfoTvTemperature.text = placeInfo.temperature.toString()
            }
        }
    }
}