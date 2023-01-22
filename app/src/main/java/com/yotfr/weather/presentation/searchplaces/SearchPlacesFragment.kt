package com.yotfr.weather.presentation.searchplaces

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yotfr.weather.R
import com.yotfr.weather.appComponent
import com.yotfr.weather.databinding.FragmentSearchPlacesBinding
import com.yotfr.weather.domain.model.PlaceInfo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchPlacesFragment : Fragment(R.layout.fragment_search_places) {
    private var _binding: FragmentSearchPlacesBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationInfoAdapter: LocationInfoAdapter

    private val viewModel: SearchPlacesViewModel by viewModels {
        requireContext().appComponent.viewModelFavtory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchPlacesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentCityManagementToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val layoutManager = LinearLayoutManager(requireContext())
        locationInfoAdapter = LocationInfoAdapter()
        locationInfoAdapter.attachDelegate(
            delegate = object : SearchedPlacesDelegate {
                override fun addPlaceClicked(place: PlaceInfo) {
                    viewModel.onEvent(
                        SearchPlacesEvent.AddPlaceToFavorite(
                            place = place
                        )
                    )
                }
                override fun placeClicked(place: PlaceInfo) {
                }
            }
        )
        binding.fragmentCityManagementRvInfos.adapter = locationInfoAdapter
        binding.fragmentCityManagementRvInfos.layoutManager = layoutManager

        binding.fragmentCityManagementSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.onEvent(
                SearchPlacesEvent.SearchQueryChanged(
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
