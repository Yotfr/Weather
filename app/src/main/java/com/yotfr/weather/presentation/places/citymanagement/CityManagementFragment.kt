package com.yotfr.weather.presentation.places.citymanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.yotfr.weather.R
import com.yotfr.weather.appComponent
import com.yotfr.weather.databinding.FragmentCityManagementBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CityManagementFragment : Fragment(R.layout.fragment_city_management) {
    private var _binding: FragmentCityManagementBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationInfoAdapter: LocationInfoAdapter

    private val viewModel: CityManagementViewModel by viewModels {
        requireContext().appComponent.viewModelFavtory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        locationInfoAdapter = LocationInfoAdapter()
        binding.fragmentCityManagementRvInfos.adapter = locationInfoAdapter
        binding.fragmentCityManagementRvInfos.layoutManager = layoutManager

        binding.fragmentCityManagementSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.onEvent(
                CityManagementEvent.SearchQueryChanged(
                    newSearchQuery = text.toString()
                )
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    locationInfoAdapter.submitList(state.rvList)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
