package com.yotfr.weather.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.yotfr.weather.R
import com.yotfr.weather.appComponent
import com.yotfr.weather.databinding.FragmentSettingsBinding
import com.yotfr.weather.domain.model.TemperatureUnits
import com.yotfr.weather.domain.model.WindSpeedUnits
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels {
        requireContext().appComponent.viewModelFavtory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentSettingsToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.fragmentSettingsTemperatureUnitsValueTv.setOnClickListener { v ->
            showTemperatureUnitsMenu(v, R.menu.temperature_units_popup_menu)
        }
        binding.fragmentSettingsWindSpeedUnitsValueTv.setOnClickListener { v ->
            showWindSpeedUnitsMenu(v, R.menu.wind_speed_units_popup_menu)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    binding.apply {
                        fragmentSettingsTemperatureUnitsValueTv.text = state.temperatureUnits.stringName
                        fragmentSettingsWindSpeedUnitsValueTv.text = state.windSpeedUnits.stringName
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showTemperatureUnitsMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.celsius -> {
                    viewModel.onEvent(
                        SettingsEvent.TemperatureUnitsChanged(
                            newTemperatureUnit = TemperatureUnits.CELSIUS
                        )
                    )
                    popup.dismiss()
                    true
                }
                R.id.fahrenheit -> {
                    viewModel.onEvent(
                        SettingsEvent.TemperatureUnitsChanged(
                            newTemperatureUnit = TemperatureUnits.FAHRENHEIT
                        )
                    )
                    popup.dismiss()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun showWindSpeedUnitsMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.kmh -> {
                    viewModel.onEvent(
                        SettingsEvent.WindSpeedUnitsChanged(
                            newWindSpeedUnits = WindSpeedUnits.KMH
                        )
                    )
                    popup.dismiss()
                    true
                }
                R.id.ms -> {
                    viewModel.onEvent(
                        SettingsEvent.WindSpeedUnitsChanged(
                            newWindSpeedUnits = WindSpeedUnits.MS
                        )
                    )
                    popup.dismiss()
                    true
                }
                R.id.mph -> {
                    viewModel.onEvent(
                        SettingsEvent.WindSpeedUnitsChanged(
                            newWindSpeedUnits = WindSpeedUnits.MPH
                        )
                    )
                    popup.dismiss()
                    true
                }
                R.id.kn -> {
                    viewModel.onEvent(
                        SettingsEvent.WindSpeedUnitsChanged(
                            newWindSpeedUnits = WindSpeedUnits.KN
                        )
                    )
                    popup.dismiss()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}
