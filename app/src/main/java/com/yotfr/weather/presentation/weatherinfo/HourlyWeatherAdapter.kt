package com.yotfr.weather.presentation.weatherinfo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yotfr.weather.databinding.ItemHourlyWeatherBinding
import com.yotfr.weather.domain.model.WeatherData
import java.time.format.DateTimeFormatter

class HourlyWeatherDiffUtilCallback : DiffUtil.ItemCallback<WeatherData>() {
    override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return oldItem.time == newItem.time
    }

    override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return oldItem == newItem
    }
}

class HourlyWeatherAdapter : ListAdapter<WeatherData, HourlyWeatherAdapter.HourlyWeatherViewHolder>(
    HourlyWeatherDiffUtilCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        return HourlyWeatherViewHolder(
            binding = ItemHourlyWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            context = parent.context
        )
    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HourlyWeatherViewHolder(
        private val binding: ItemHourlyWeatherBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherData: WeatherData) {
            binding.apply {
                itemHourlyWeatherTvTime.text =
                    weatherData.time.format(DateTimeFormatter.ofPattern("HH:mm"))
                itemHourlyWeatherTvTemperature.text = weatherData.temperature.toString()
                itemHourlyWeatherIvWeatherType.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        weatherData.weatherType.iconRes
                    )
                )
            }
        }
    }
}
