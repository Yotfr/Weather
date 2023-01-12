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
import com.yotfr.weather.presentation.utils.MarginItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class WeatherInfoFragment : Fragment(R.layout.fragment_weather_info) {

    private var _binding: FragmentWeatherInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var todayAdapter: HourlyWeatherAdapter
    private lateinit var tomorrowAdapter: HourlyWeatherAdapter
    private lateinit var afterTomorrowAdapter: HourlyWeatherAdapter
    private lateinit var inTwoDaysAdapter: HourlyWeatherAdapter
    private lateinit var inThreeDaysAdapter: HourlyWeatherAdapter
    private lateinit var inFourDaysAdapter: HourlyWeatherAdapter
    private lateinit var inFiveDaysAdapter: HourlyWeatherAdapter

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

        val todayLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        todayAdapter = HourlyWeatherAdapter()
        binding.fragmentWeatherInfoRvTodayWeather.adapter = todayAdapter
        binding.fragmentWeatherInfoRvTodayWeather.layoutManager = todayLayoutManager
        binding.fragmentWeatherInfoRvTodayWeather.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        val tomorrowLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        tomorrowAdapter = HourlyWeatherAdapter()
        binding.fragmentWeatherInfoRvTomorrowWeather.adapter = tomorrowAdapter
        binding.fragmentWeatherInfoRvTomorrowWeather.layoutManager = tomorrowLayoutManager
        binding.fragmentWeatherInfoRvTomorrowWeather.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        val afterTomorrowLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        afterTomorrowAdapter = HourlyWeatherAdapter()
        binding.fragmentWeatherInfoRvAfterTomorrowWeather.adapter = afterTomorrowAdapter
        binding.fragmentWeatherInfoRvAfterTomorrowWeather.layoutManager = afterTomorrowLayoutManager
        binding.fragmentWeatherInfoRvAfterTomorrowWeather.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        val inTwoDaysLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        inTwoDaysAdapter = HourlyWeatherAdapter()
        binding.fragmentWeatherInfoRvInTwoDaysWeather.adapter = inTwoDaysAdapter
        binding.fragmentWeatherInfoRvInTwoDaysWeather.layoutManager = inTwoDaysLayoutManager
        binding.fragmentWeatherInfoRvInTwoDaysWeather.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        val inThreeDaysLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        inThreeDaysAdapter = HourlyWeatherAdapter()
        binding.fragmentWeatherInfoRvInThreeDaysWeather.adapter = inThreeDaysAdapter
        binding.fragmentWeatherInfoRvInThreeDaysWeather.layoutManager = inThreeDaysLayoutManager
        binding.fragmentWeatherInfoRvInThreeDaysWeather.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        val inFourDaysLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        inFourDaysAdapter = HourlyWeatherAdapter()
        binding.fragmentWeatherInfoRvInFourDaysWeather.adapter = inFourDaysAdapter
        binding.fragmentWeatherInfoRvInFourDaysWeather.layoutManager = inFourDaysLayoutManager
        binding.fragmentWeatherInfoRvInFourDaysWeather.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        val inFiveDaysLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        inFiveDaysAdapter = HourlyWeatherAdapter()
        binding.fragmentWeatherInfoRvInFiveDaysWeather.adapter = inFiveDaysAdapter
        binding.fragmentWeatherInfoRvInFiveDaysWeather.layoutManager = inFiveDaysLayoutManager
        binding.fragmentWeatherInfoRvInFiveDaysWeather.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

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
                    state.weatherInfo?.detailedWeatherDataPerDay?.get(0)?.let { weatherDataList ->
                        todayAdapter.submitList(weatherDataList)
                        binding.fragmentWeatherInfoTvTodayDate.text = getString(R.string.today)
                    }
                    state.weatherInfo?.briefWeatherDataPerDay?.get(1)?.let { weatherDataList ->
                        tomorrowAdapter.submitList(weatherDataList)
                        binding.fragmentWeatherInfoTvTomorrowDate.text = getString(R.string.tomorrow)
                    }
                    state.weatherInfo?.briefWeatherDataPerDay?.get(2)?.let { weatherDataList ->
                        afterTomorrowAdapter.submitList(weatherDataList)
                        binding.fragmentWeatherInfoTvAfterTomorrowDate.text = weatherDataList[0].time.format(
                            DateTimeFormatter.ofPattern("MMMM d")
                        )
                    }
                    state.weatherInfo?.briefWeatherDataPerDay?.get(3)?.let { weatherDataList ->
                        inTwoDaysAdapter.submitList(weatherDataList)
                        binding.fragmentWeatherInfoTvInTwoDaysDate.text = weatherDataList[0].time.format(
                            DateTimeFormatter.ofPattern("MMMM d")
                        )
                    }
                    state.weatherInfo?.briefWeatherDataPerDay?.get(4)?.let { weatherDataList ->
                        inThreeDaysAdapter.submitList(weatherDataList)
                        binding.fragmentWeatherInfoTvInThreeDaysDate.text = weatherDataList[0].time.format(
                            DateTimeFormatter.ofPattern("MMMM d")
                        )
                    }
                    state.weatherInfo?.briefWeatherDataPerDay?.get(5)?.let { weatherDataList ->
                        inFourDaysAdapter.submitList(weatherDataList)
                        binding.fragmentWeatherInfoTvInFourDaysDate.text = weatherDataList[0].time.format(
                            DateTimeFormatter.ofPattern("MMMM d")
                        )
                    }
                    state.weatherInfo?.briefWeatherDataPerDay?.get(6)?.let { weatherDataList ->
                        inFiveDaysAdapter.submitList(weatherDataList)
                        binding.fragmentWeatherInfoTvInFiveDaysDate.text = weatherDataList[0].time.format(
                            DateTimeFormatter.ofPattern("MMMM d")
                        )
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
