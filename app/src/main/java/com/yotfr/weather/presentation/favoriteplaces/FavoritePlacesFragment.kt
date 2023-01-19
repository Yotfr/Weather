package com.yotfr.weather.presentation.favoriteplaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yotfr.weather.R
import com.yotfr.weather.appComponent
import com.yotfr.weather.databinding.FragmentFavoritePlacesBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoritePlacesFragment : Fragment(R.layout.fragment_favorite_places) {
    private var _binding: FragmentFavoritePlacesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FavoritePlacesAdapter

    private val viewModel: FavoritePlacesViewModel by viewModels {
        requireContext().appComponent.viewModelFavtory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritePlacesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        adapter = FavoritePlacesAdapter()

        binding.favoritePlacesRv.adapter = adapter
        binding.favoritePlacesRv.layoutManager = layoutManager

        binding.toSearch.setOnClickListener {
            val action = FavoritePlacesFragmentDirections
                .actionFavoritePlacesFragmentToSearchPlacesFragment()
            findNavController().navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest {
                    adapter.submitList(it.rvList)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
