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

        initializeAdapters()

        processUiState()
    }

    private fun processUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    binding.apply {
                        fragmentWeatherInfoTvCurrentTime.text = state.currentTime
                        fragmentWeatherInfoIvWeatherTypeIcon.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                state.currentWeatherTypeIconRes
                            )
                        )
                        fragmentWeatherInfoTvWeatherType.text = state.currentWeatherTypeDescription
                        fragmentWeatherInfoTvTemperature.text = state.currentTemperature
                        fragmentWeatherInfoTvPressure.text = state.currentPressure
                        fragmentWeatherInfoTvHumidity.text = state.currentHumidity
                        fragmentWeatherInfoTvWindSpeed.text = state.currentWindSpeed
                        fragmentWeatherInfoTvTodayDate.text = getString(R.string.today)
                        fragmentWeatherInfoTvTomorrowDate.text = getString(R.string.tomorrow)
                        binding.fragmentWeatherInfoTvAfterTomorrowDate.text =
                            state.dayAfterTomorrowDate
                        binding.fragmentWeatherInfoTvInTwoDaysDate.text = state.inTwoDaysDate
                        binding.fragmentWeatherInfoTvInThreeDaysDate.text = state.inThreeDaysDate
                        binding.fragmentWeatherInfoTvInFourDaysDate.text = state.inFourDaysDate
                        binding.fragmentWeatherInfoTvInFiveDaysDate.text = state.inFiveDaysDate
                    }
                    todayAdapter.submitList(state.hourlyWeatherListForToday)
                    tomorrowAdapter.submitList(state.hourlyWeatherListForTomorrow)
                    afterTomorrowAdapter.submitList(state.hourlyWeatherListForDayAfterTomorrow)
                    inTwoDaysAdapter.submitList(state.hourlyWeatherListForInTwoDays)
                    inThreeDaysAdapter.submitList(state.hourlyWeatherListForInThreeDays)
                    inFourDaysAdapter.submitList(state.hourlyWeatherListForInFourDays)
                    inFiveDaysAdapter.submitList(state.hourlyWeatherListForInFiveDays)
                }
            }
        }
    }

    private fun initializeAdapters() {
        val todayLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        todayAdapter = HourlyWeatherAdapter()
        binding.fragmentWeatherInfoRvTodayWeather.adapter = todayAdapter
        binding.fragmentWeatherInfoRvTodayWeather.layoutManager = todayLayoutManager
        binding.fragmentWeatherInfoRvTodayWeather.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        val tomorrowLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        tomorrowAdapter = HourlyWeatherAdapter()
        binding.fragmentWeatherInfoRvTomorrowWeather.adapter = tomorrowAdapter
        binding.fragmentWeatherInfoRvTomorrowWeather.layoutManager = tomorrowLayoutManager
        binding.fragmentWeatherInfoRvTomorrowWeather.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        val afterTomorrowLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        afterTomorrowAdapter = HourlyWeatherAdapter()
        binding.fragmentWeatherInfoRvAfterTomorrowWeather.adapter = afterTomorrowAdapter
        binding.fragmentWeatherInfoRvAfterTomorrowWeather.layoutManager = afterTomorrowLayoutManager
        binding.fragmentWeatherInfoRvAfterTomorrowWeather.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        val inTwoDaysLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        inTwoDaysAdapter = HourlyWeatherAdapter()
        binding.fragmentWeatherInfoRvInTwoDaysWeather.adapter = inTwoDaysAdapter
        binding.fragmentWeatherInfoRvInTwoDaysWeather.layoutManager = inTwoDaysLayoutManager
        binding.fragmentWeatherInfoRvInTwoDaysWeather.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        val inThreeDaysLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        inThreeDaysAdapter = HourlyWeatherAdapter()
        binding.fragmentWeatherInfoRvInThreeDaysWeather.adapter = inThreeDaysAdapter
        binding.fragmentWeatherInfoRvInThreeDaysWeather.layoutManager = inThreeDaysLayoutManager
        binding.fragmentWeatherInfoRvInThreeDaysWeather.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        val inFourDaysLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        inFourDaysAdapter = HourlyWeatherAdapter()
        binding.fragmentWeatherInfoRvInFourDaysWeather.adapter = inFourDaysAdapter
        binding.fragmentWeatherInfoRvInFourDaysWeather.layoutManager = inFourDaysLayoutManager
        binding.fragmentWeatherInfoRvInFourDaysWeather.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        val inFiveDaysLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        inFiveDaysAdapter = HourlyWeatherAdapter()
        binding.fragmentWeatherInfoRvInFiveDaysWeather.adapter = inFiveDaysAdapter
        binding.fragmentWeatherInfoRvInFiveDaysWeather.layoutManager = inFiveDaysLayoutManager
        binding.fragmentWeatherInfoRvInFiveDaysWeather.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
