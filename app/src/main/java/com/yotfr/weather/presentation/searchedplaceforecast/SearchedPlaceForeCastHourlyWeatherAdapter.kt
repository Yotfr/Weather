package com.yotfr.weather.presentation.searchedplaceforecast

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yotfr.weather.databinding.ItemHourlyWeatherBinding
import com.yotfr.weather.domain.model.HourlyWeatherData
import com.yotfr.weather.domain.model.TemperatureUnits
import com.yotfr.weather.presentation.utils.getIconRes
import com.yotfr.weather.presentation.utils.toTemperatureUnitString
import java.time.format.DateTimeFormatter

class SearchedPlaceForecastHourlyWeatherDiffUtilCallback :
    DiffUtil.ItemCallback<HourlyWeatherData>() {
    override fun areItemsTheSame(oldItem: HourlyWeatherData, newItem: HourlyWeatherData): Boolean {
        return oldItem.time == newItem.time
    }

    override fun areContentsTheSame(
        oldItem: HourlyWeatherData,
        newItem: HourlyWeatherData
    ): Boolean {
        return oldItem == newItem
    }
}

class SearchedPlaceForeCastHourlyWeatherAdapter :
    ListAdapter<HourlyWeatherData, SearchedPlaceForeCastHourlyWeatherAdapter.SearchedPlaceForecastHourlyWeatherViewHolder>(
        SearchedPlaceForecastHourlyWeatherDiffUtilCallback()
    ) {

    private var temperatureUnit: TemperatureUnits? = null

    fun attachTemperatureUnit(temperatureUnits: TemperatureUnits) {
        this.temperatureUnit = temperatureUnits
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchedPlaceForecastHourlyWeatherViewHolder {
        return SearchedPlaceForecastHourlyWeatherViewHolder(
            binding = ItemHourlyWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            context = parent.context,
            temperatureUnits = temperatureUnit
        )
    }

    override fun onBindViewHolder(
        holder: SearchedPlaceForecastHourlyWeatherViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class SearchedPlaceForecastHourlyWeatherViewHolder(
        private val binding: ItemHourlyWeatherBinding,
        private val context: Context,
        private val temperatureUnits: TemperatureUnits?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hourlyWeatherData: HourlyWeatherData) {
            binding.apply {
                itemHourlyWeatherTvTime.text =
                    hourlyWeatherData.time.format(DateTimeFormatter.ofPattern("HH:mm"))
                temperatureUnits?.let {
                    itemHourlyWeatherTvTemperature.text =
                        hourlyWeatherData.temperature.toString().toTemperatureUnitString(
                            temperatureUnit = it
                        )
                } ?: run {
                    itemHourlyWeatherTvTemperature.text = hourlyWeatherData.temperature.toString()
                }
                itemHourlyWeatherIvWeatherType.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        hourlyWeatherData.weatherType.getIconRes()
                    )
                )
            }
        }
    }
}
