package com.yotfr.weather.presentation.currentdayforecast

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yotfr.weather.databinding.ItemHourlyWeatherBinding
import com.yotfr.weather.domain.model.HourlyWeatherData
import java.time.format.DateTimeFormatter

class HourlyWeatherDiffUtilCallback : DiffUtil.ItemCallback<HourlyWeatherData>() {
    override fun areItemsTheSame(oldItem: HourlyWeatherData, newItem: HourlyWeatherData): Boolean {
        return oldItem.time == newItem.time
    }

    override fun areContentsTheSame(oldItem: HourlyWeatherData, newItem: HourlyWeatherData): Boolean {
        return oldItem == newItem
    }
}

class HourlyWeatherAdapter : ListAdapter<HourlyWeatherData, HourlyWeatherAdapter.HourlyWeatherViewHolder>(
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
        fun bind(hourlyWeatherData: HourlyWeatherData) {
            binding.apply {
                itemHourlyWeatherTvTime.text =
                    hourlyWeatherData.time.format(DateTimeFormatter.ofPattern("HH:mm"))
                itemHourlyWeatherTvTemperature.text = hourlyWeatherData.temperature.toString()
                itemHourlyWeatherIvWeatherType.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        hourlyWeatherData.weatherType.iconRes
                    )
                )
            }
        }
    }
}
