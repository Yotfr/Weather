package com.yotfr.weather.presentation.favoriteplaces

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yotfr.weather.databinding.ItemFavoritePlaceInfoBinding
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.model.TemperatureUnits
import com.yotfr.weather.presentation.utils.getIconRes
import com.yotfr.weather.presentation.utils.toTemperatureUnitString

interface FavoritePlacesDelegate {
    fun placeClicked(placeId: Long)
    fun deleteClicked(placeInfo: FavoritePlaceInfo)
}

class FavoritePlacesDiffUtilCallback : DiffUtil.ItemCallback<FavoritePlaceInfo>() {
    override fun areItemsTheSame(oldItem: FavoritePlaceInfo, newItem: FavoritePlaceInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: FavoritePlaceInfo,
        newItem: FavoritePlaceInfo
    ): Boolean {
        return oldItem == newItem
    }
}

class FavoritePlacesAdapter :
    ListAdapter<FavoritePlaceInfo, FavoritePlacesAdapter.FavoritePlaceViewHolder>(
        FavoritePlacesDiffUtilCallback()
    ) {

    private var delegate: FavoritePlacesDelegate? = null

    fun attachDelegate(delegate: FavoritePlacesDelegate) {
        this.delegate = delegate
    }

    private var temperatureUnit: TemperatureUnits? = null

    fun attachTemperatureUnit(temperatureUnits: TemperatureUnits) {
        this.temperatureUnit = temperatureUnits
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePlaceViewHolder {
        return FavoritePlaceViewHolder(
            binding = ItemFavoritePlaceInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            context = parent.context,
            delegate = delegate,
            temperatureUnits = temperatureUnit
        )
    }

    override fun onBindViewHolder(holder: FavoritePlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FavoritePlaceViewHolder(
        private val binding: ItemFavoritePlaceInfoBinding,
        private val context: Context,
        private val delegate: FavoritePlacesDelegate?,
        private val temperatureUnits: TemperatureUnits?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(placeInfo: FavoritePlaceInfo) {
            binding.apply {
                itemFavoritePlaceInfoTvPlaceName.text = placeInfo.placeName
                itemFavoritePlaceInfoTvCountryName.text = placeInfo.countryName
                placeInfo.weatherInfo?.let { weatherInfo ->
                    itemFavoritePlaceInfoIvWeatherType.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            weatherInfo.currentWeatherData.weatherType.getIconRes()
                        )
                    )
                    temperatureUnits?.let {
                        itemFavoritePlaceInfoTvTemperature.text = weatherInfo.currentWeatherData
                            .temperature.toString().toTemperatureUnitString(
                                temperatureUnit = it
                            )
                    } ?: run {
                        itemFavoritePlaceInfoTvTemperature.text = weatherInfo.currentWeatherData
                            .temperature.toString()
                    }
                }
                if (placeInfo.id == -2L) {
                    delete.visibility = View.GONE
                } else {
                    delete.visibility = View.VISIBLE
                }
                delete.setOnClickListener {
                    delegate?.deleteClicked(
                        placeInfo = placeInfo
                    )
                }
                itemFavoritePlaceInfo.setOnClickListener {
                    delegate?.placeClicked(
                        placeId = placeInfo.id
                    )
                }
            }
        }
    }
}
