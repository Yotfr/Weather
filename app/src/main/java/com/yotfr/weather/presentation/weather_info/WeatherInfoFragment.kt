package com.yotfr.weather.presentation.weather_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.yotfr.weather.R
import com.yotfr.weather.appComponent
import com.yotfr.weather.databinding.FragmentWeatherInfoBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class WeatherInfoFragment : Fragment(R.layout.fragment_weather_info) {

    private var _binding: FragmentWeatherInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HourlyWeatherAdapter

    private val viewModel: WeatherInfoViewModel by viewModels {
        requireContext().appComponent.viewModelFavtory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = HourlyWeatherAdapter()
        binding.fragmentWeatherInfoRvHourlyWeather.adapter = adapter
        binding.fragmentWeatherInfoRvHourlyWeather.layoutManager = layoutManager

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    state.weatherInfo?.currentWeatherData?.let { weatherData ->
                        binding.fragmentWeatherInfoTvCurrentTime.text = weatherData.time.format(
                            DateTimeFormatter.ofPattern("HH:mm")
                        )
                        binding.fragmentWeatherInfoIvWeatherTypeIcon.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                weatherData.weatherType.iconRes
                            )
                        )
                        binding.fragmentWeatherInfoTvWeatherType.text =
                            weatherData.weatherType.weatherDesc
                        binding.fragmentWeatherInfoTvTemperature.text =
                            weatherData.temperature.toString()
                        binding.fragmentWeatherInfoTvPressure.text = weatherData.pressure.toString()
                        binding.fragmentWeatherInfoTvHumidity.text = weatherData.humidity.toString()
                        binding.fragmentWeatherInfoTvWindSpeed.text =
                            weatherData.windSpeed.toString()
                    }
                    state.weatherInfo?.weatherDataPerDay?.get(0)?.let { weatherDataList ->
                        adapter.submitList(weatherDataList)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
