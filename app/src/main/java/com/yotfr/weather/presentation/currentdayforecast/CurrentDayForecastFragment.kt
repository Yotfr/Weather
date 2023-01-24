package com.yotfr.weather.presentation.currentdayforecast

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
import com.yotfr.weather.databinding.FragmentCurrentDayForecastBinding
import com.yotfr.weather.presentation.utils.MarginItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CurrentDayForecastFragment : Fragment(R.layout.fragment_current_day_forecast) {

    private var _binding: FragmentCurrentDayForecastBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HourlyWeatherAdapter

    private val viewModel: CurrentDayForecastViewModel by viewModels {
        requireContext().appComponent.viewModelFavtory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentDayForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("placeId")?.observe(
            viewLifecycleOwner
        ) { placeId ->
            viewModel.onEvent(
                CurrentDayForecastEvent.ChangeCurrentSelectedPlaceId(
                    newPlaceId = placeId.toLong()
                )
            )
        }

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = HourlyWeatherAdapter()
        binding.fragmentCurrentWeatherInfoRvHourlyWeather.adapter = adapter
        binding.fragmentCurrentWeatherInfoRvHourlyWeather.layoutManager = layoutManager
        binding.fragmentCurrentWeatherInfoRvHourlyWeather.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        binding.fragmentSevenDaysForecastToolbar.setNavigationOnClickListener {
            val action = CurrentDayForecastFragmentDirections.actionWeatherInfoFragmentToFavoritePlacesFragment()
            findNavController().navigate(action)
        }
        binding.fragmentSevenDaysForecastToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.settings -> {
                    val action = CurrentDayForecastFragmentDirections.actionWeatherInfoFragmentToSettingsFragment()
                    findNavController().navigate(action)
                    true
                }
                else -> false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    binding.apply {
                        fragmentCurrentWeatherInfoTvCurrentTime.text = state.currentTime
                        state.currentWeatherTypeIconRes?.let {
                            fragmentCurrentWeatherInfoIvWeatherType.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    it
                                )
                            )
                        }
                        fragmentSevenDaysForecastToolbar.title = state.toolbarTitle
                        fragmentCurrentWeatherInfoTvWeatherType.text = state.currentWeatherTypeDescription
                        fragmentCurrentWeatherInfoTvCurrentTemperature.text = state.currentTemperature
                        fragmentCurrentWeatherInfoTvApparentTemperature.text = state.currentApparentTemperature
                        fragmentCurrentWeatherInfoTvPressure.text = state.currentPressure
                        fragmentCurrentWeatherInfoTvHumidity.text = state.currentHumidity
                        fragmentCurrentWeatherInfoTvWindSpeed.text = state.currentWindSpeed
                        adapter.submitList(state.hourlyWeatherList)
                    }
                }
            }
        }

        binding.fragmentCurrentWeatherInfoBtnSevenDaysForecast.setOnClickListener {
            val action = CurrentDayForecastFragmentDirections.actionWeatherInfoFragmentToSevenDaysForecastFragment(
                placeId = viewModel.currentSelectedPlaceId.value
            )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
