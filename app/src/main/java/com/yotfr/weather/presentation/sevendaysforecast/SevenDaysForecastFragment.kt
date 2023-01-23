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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.yotfr.weather.R
import com.yotfr.weather.appComponent
import com.yotfr.weather.databinding.FragmentSevenDaysForecastBinding
import com.yotfr.weather.presentation.currentdayforecast.CurrentDayForecastFragmentDirections
import com.yotfr.weather.presentation.utils.MarginItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SevenDaysForecastFragment : Fragment(R.layout.fragment_seven_days_forecast) {
    private var _binding: FragmentSevenDaysForecastBinding? = null
    private val binding get() = _binding!!

    private val args: SevenDaysForecastFragmentArgs by navArgs()

    private lateinit var adapter: SevenDaysForecastHourlyWeatherAdapter

    private val viewModel: SevenDaysForecastViewModel by viewModels {
        requireContext().appComponent.viewModelFavtory()
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

        val placeId = args.placeId
        viewModel.onEvent(
            SevenDaysForecastEvent.ChangeCurrentSelectedPlaceId(
                newPlaceId = placeId
            )
        )

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = SevenDaysForecastHourlyWeatherAdapter()
        binding.reused.fragmentSevenDaysForecastRvHourlyWeather.adapter = adapter
        binding.reused.fragmentSevenDaysForecastRvHourlyWeather.layoutManager = layoutManager
        binding.reused.fragmentSevenDaysForecastRvHourlyWeather.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        binding.fragmentSevenDaysForecastToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.fragmentSevenDaysForecastToolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.settings -> {
                    val action = SevenDaysForecastFragmentDirections.actionSevenDaysForecastFragmentToSettingsFragment()
                    findNavController().navigate(action)
                    true
                }
                else -> false
            }
        }

        binding.reused.todayBtn.setOnClickListener {
            viewModel.onEvent(
                SevenDaysForecastEvent.SelectedDayIndexChanged(
                    newIndex = 0
                )
            )
        }
        binding.reused.tomorrowBtn.setOnClickListener {
            viewModel.onEvent(
                SevenDaysForecastEvent.SelectedDayIndexChanged(
                    newIndex = 1
                )
            )
        }
        binding.reused.afterTomorrowBtn.setOnClickListener {
            viewModel.onEvent(
                SevenDaysForecastEvent.SelectedDayIndexChanged(
                    newIndex = 2
                )
            )
        }
        binding.reused.inTwoDaysBtn.setOnClickListener {
            viewModel.onEvent(
                SevenDaysForecastEvent.SelectedDayIndexChanged(
                    newIndex = 3
                )
            )
        }
        binding.reused.inThreeDaysBtn.setOnClickListener {
            viewModel.onEvent(
                SevenDaysForecastEvent.SelectedDayIndexChanged(
                    newIndex = 4
                )
            )
        }
        binding.reused.inFourDaysBtn.setOnClickListener {
            viewModel.onEvent(
                SevenDaysForecastEvent.SelectedDayIndexChanged(
                    newIndex = 5
                )
            )
        }
        binding.reused.inFiveDaysBtn.setOnClickListener {
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
                        fragmentSevenDaysForecastToolbar.title = state.toolbarTitle
                        reused.fragmentSevenDaysForecastTvDate.text = state.selectedDate
                        reused.fragmentSevenDaysForecastTvMaxTemperature.text =
                            state.selectedMaxTemperature
                        reused.fragmentSevenDaysForecastTvMinTemperature.text =
                            state.selectedMinTemperature
                        state.selectedWeatherType?.let { resId ->
                            reused.fragmentSevenDaysForecastIvWeatherType.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    resId
                                )
                            )
                        }
                        reused.fragmentSevenDaysForecastTvSunrise.text = state.sunriseTime
                        reused.fragmentSevenDaysForecastTvSunset.text = state.sunsetTime
                        adapter.submitList(state.selectedDayHourlyWeatherList)
                        reused.todayItemDate.text = state.todayDate
                        reused.todayItemMaxTemperature.text = state.todayMaxTemperature
                        reused.todayItemMinTemperature.text = state.todayMinTemperature
                        state.todayWeatherType?.let { resId ->
                            reused.todayItemWeatherType.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    resId
                                )
                            )
                        }
                        reused.tomorrowItemDate.text = state.tomorrowDate
                        reused.tomorrowItemMaxTemperature.text = state.tomorrowMaxTemperature
                        reused.tomorrowItemMinTemperature.text = state.tomorrowMinTemperature
                        state.tomorrowWeatherType?.let { resId ->
                            reused.tomorrowItemWeatherType.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    resId
                                )
                            )
                        }
                        reused.afterTomorrowItemDate.text = state.afterTomorrowDate
                        reused.afterTomorrowDayOfWeek.text = state.afterTomorrowDayOfWeek
                        reused.afterTomorrowItemMaxTemperature.text = state.afterTomorrowMaxTemperature
                        reused.afterTomorrowItemMinTemperature.text = state.afterTomorrowMinTemperature
                        state.afterTomorrowWeatherType?.let { resId ->
                            reused.afterTomorrowItemIvWeatherType.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    resId
                                )
                            )
                        }
                        reused.inTwoDaysItemDate.text = state.inTwoDaysDate
                        reused.inTwoDaysDayOfWeek.text = state.inTwoDaysDayOfWeek
                        reused.inTwoDaysItemMaxTemperature.text = state.inTwoDaysMaxTemperature
                        reused.inTwoDaysItemMinTemperature.text = state.inTwoDaysMinTemperature
                        state.inTwoDaysWeatherType?.let { resId ->
                            reused.inTwoDaysItemIvWeatherType.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    resId
                                )
                            )
                        }
                        reused.inThreeDaysItemDate.text = state.inThreeDaysDate
                        reused.inThreeDaysDayOfWeek.text = state.inThreeDaysDayOfWeek
                        reused.inThreeDaysItemMaxTemperature.text = state.inThreeDaysMaxTemperature
                        reused.inThreeDaysItemMinTemperature.text = state.inThreeDaysMinTemperature
                        state.inThreeDaysWeatherType?.let { resId ->
                            reused.inThreeDaysItemIvWeatherType.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    resId
                                )
                            )
                        }
                        reused.inFourDaysItemDate.text = state.inFourDaysDate
                        reused.inFourDaysDayOfWeek.text = state.inFourDaysDayOfWeek
                        reused.inFourDaysItemMaxTemperature.text = state.inFourDaysMaxTemperature
                        reused.inFourDaysItemMinTemperature.text = state.inFourDaysMinTemperature
                        state.inFourDaysWeatherType?.let { resId ->
                            reused.inFourDaysItemIvWeatherType.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    resId
                                )
                            )
                        }
                        reused.inFiveDaysItemDate.text = state.inFiveDaysDate
                        reused.inFiveDaysDayOfWeek.text = state.inFiveDaysDayOfWeek
                        reused.inFiveDaysItemMaxTemperature.text = state.inFiveDaysMaxTemperature
                        reused.inFiveDaysItemMinTemperature.text = state.inFiveDaysMinTemperature
                        state.inFiveDaysWeatherType?.let { resId ->
                            reused.inFiveDaysItemIvWeatherType.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    resId
                                )
                            )
                        }
                        reused.todayItem.isEnabled = false
                        reused.tomorrowItem.isEnabled = false
                        reused.afterTomorrowItem.isEnabled = false
                        reused.inTwoDaysItem.isEnabled = false
                        reused.inThreeDaysItem.isEnabled = false
                        reused.inFourDaysItem.isEnabled = false
                        reused.inFiveDaysItem.isEnabled = false
                    }
                }
                viewModel.selectedIndex.collectLatest { index ->
                    binding.apply {
                        when (index) {
                            0 -> {
                                reused.todayItem.isEnabled = true
                            }
                            1 -> {
                                reused.tomorrowItem.isEnabled = true
                            }
                            2 -> {
                                reused.afterTomorrowItem.isEnabled = true
                            }
                            3 -> {
                                reused.inTwoDaysItem.isEnabled = true
                            }
                            4 -> {
                                reused.inThreeDaysItem.isEnabled = true
                            }
                            5 -> {
                                reused.inFourDaysItem.isEnabled = true
                            }
                            6 -> {
                                reused.inFiveDaysItem.isEnabled = true
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
