package com.yotfr.weather.presentation.sevendaysforecast

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yotfr.weather.R
import com.yotfr.weather.appComponent
import com.yotfr.weather.databinding.FragmentSevenDaysForecastBinding
import com.yotfr.weather.presentation.utils.MarginItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SevenDaysForecastFragment : Fragment(R.layout.fragment_seven_days_forecast) {
    private var _binding: FragmentSevenDaysForecastBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SevenDaysForecastHourlyWeatherAdapter

    private val viewModel: SevenDaysForecastViewModel by viewModels {
        SevenDaysForecastViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSevenDaysForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = SevenDaysForecastHourlyWeatherAdapter()
        binding.fragmentSevenDaysForecastRvHourlyWeather.adapter = adapter
        binding.fragmentSevenDaysForecastRvHourlyWeather.layoutManager = layoutManager
        binding.fragmentSevenDaysForecastRvHourlyWeather.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        binding.fragmentSevenDaysForecastToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.todayBtn.setOnClickListener {
            viewModel.onEvent(
                SevenDaysForecastEvent.SelectedDayIndexChanged(
                    newIndex = 0
                )
            )
        }
        binding.tomorrowBtn.setOnClickListener {
            viewModel.onEvent(
                SevenDaysForecastEvent.SelectedDayIndexChanged(
                    newIndex = 1
                )
            )
        }
        binding.afterTomorrowBtn.setOnClickListener {
            viewModel.onEvent(
                SevenDaysForecastEvent.SelectedDayIndexChanged(
                    newIndex = 2
                )
            )
        }
        binding.inTwoDaysBtn.setOnClickListener {
            viewModel.onEvent(
                SevenDaysForecastEvent.SelectedDayIndexChanged(
                    newIndex = 3
                )
            )
        }
        binding.inThreeDaysBtn.setOnClickListener {
            viewModel.onEvent(
                SevenDaysForecastEvent.SelectedDayIndexChanged(
                    newIndex = 4
                )
            )
        }
        binding.inFourDaysBtn.setOnClickListener {
            viewModel.onEvent(
                SevenDaysForecastEvent.SelectedDayIndexChanged(
                    newIndex = 5
                )
            )
        }
        binding.inFiveDaysBtn.setOnClickListener {
            viewModel.onEvent(
                SevenDaysForecastEvent.SelectedDayIndexChanged(
                    newIndex = 6
                )
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    binding.apply {
                        fragmentSevenDaysForecastTvDate.text = state.selectedDate
                        fragmentSevenDaysForecastTvMaxTemperature.text =
                            state.selectedMaxTemperature
                        fragmentSevenDaysForecastTvMinTemperature.text =
                            state.selectedMinTemperature
                        state.selectedWeatherType?.let { resId ->
                            fragmentSevenDaysForecastIvWeatherType.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    resId
                                )
                            )
                        }
                        fragmentSevenDaysForecastTvSunrise.text = state.sunriseTime
                        fragmentSevenDaysForecastTvSunset.text = state.sunsetTime
                        adapter.submitList(state.selectedDayHourlyWeatherList)
                        todayItemDate.text = state.todayDate
                        todayItemMaxTemperature.text = state.todayMaxTemperature
                        todayItemMinTemperature.text = state.todayMinTemperature
                        state.todayWeatherType?.let { resId ->
                            todayItemWeatherType.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    resId
                                )
                            )
                        }
                        tomorrowItemDate.text = state.tomorrowDate
                        tomorrowItemMaxTemperature.text = state.tomorrowMaxTemperature
                        tomorrowItemMinTemperature.text = state.tomorrowMinTemperature
                        state.tomorrowWeatherType?.let { resId ->
                            tomorrowItemWeatherType.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    resId
                                )
                            )
                        }
                        afterTomorrowItemDate.text = state.afterTomorrowDate
                        afterTomorrowDayOfWeek.text = state.afterTomorrowDayOfWeek
                        afterTomorrowItemMaxTemperature.text = state.afterTomorrowMaxTemperature
                        afterTomorrowItemMinTemperature.text = state.afterTomorrowMinTemperature
                        state.afterTomorrowWeatherType?.let { resId ->
                            afterTomorrowItemIvWeatherType.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    resId
                                )
                            )
                        }
                        inTwoDaysItemDate.text = state.inTwoDaysDate
                        inTwoDaysDayOfWeek.text = state.inTwoDaysDayOfWeek
                        inTwoDaysItemMaxTemperature.text = state.inTwoDaysMaxTemperature
                        inTwoDaysItemMinTemperature.text = state.inTwoDaysMinTemperature
                        state.inTwoDaysWeatherType?.let { resId ->
                            inTwoDaysItemIvWeatherType.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    resId
                                )
                            )
                        }
                        inThreeDaysItemDate.text = state.inThreeDaysDate
                        inThreeDaysDayOfWeek.text = state.inThreeDaysDayOfWeek
                        inThreeDaysItemMaxTemperature.text = state.inThreeDaysMaxTemperature
                        inThreeDaysItemMinTemperature.text = state.inThreeDaysMinTemperature
                        state.inThreeDaysWeatherType?.let { resId ->
                            inThreeDaysItemIvWeatherType.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    resId
                                )
                            )
                        }
                        inFourDaysItemDate.text = state.inFourDaysDate
                        inFourDaysDayOfWeek.text = state.inFourDaysDayOfWeek
                        inFourDaysItemMaxTemperature.text = state.inFourDaysMaxTemperature
                        inFourDaysItemMinTemperature.text = state.inFourDaysMinTemperature
                        state.inFourDaysWeatherType?.let { resId ->
                            inFourDaysItemIvWeatherType.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    resId
                                )
                            )
                        }
                        inFiveDaysItemDate.text = state.inFiveDaysDate
                        inFiveDaysDayOfWeek.text = state.inFiveDaysDayOfWeek
                        inFiveDaysItemMaxTemperature.text = state.inFiveDaysMaxTemperature
                        inFiveDaysItemMinTemperature.text = state.inFiveDaysMinTemperature
                        state.inFiveDaysWeatherType?.let { resId ->
                            inFiveDaysItemIvWeatherType.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    resId
                                )
                            )
                        }
                        todayItem.isEnabled = false
                        tomorrowItem.isEnabled = false
                        afterTomorrowItem.isEnabled = false
                        inTwoDaysItem.isEnabled = false
                        inThreeDaysItem.isEnabled = false
                        inFourDaysItem.isEnabled = false
                        inFiveDaysItem.isEnabled = false
                    }
                }
                viewModel.selectedIndex.collectLatest { index ->
                    binding.apply {
                        when (index) {
                            0 -> {
                                todayItem.isEnabled = true
                            }
                            1 -> {
                                tomorrowItem.isEnabled = true
                            }
                            2 -> {
                                afterTomorrowItem.isEnabled = true
                            }
                            3 -> {
                                inTwoDaysItem.isEnabled = true
                            }
                            4 -> {
                                inThreeDaysItem.isEnabled = true
                            }
                            5 -> {
                                inFourDaysItem.isEnabled = true
                            }
                            6 -> {
                                inFiveDaysItem.isEnabled = true
                            }
                        }
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
