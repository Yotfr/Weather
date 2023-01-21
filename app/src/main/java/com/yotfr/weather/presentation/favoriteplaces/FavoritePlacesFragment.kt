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
import com.yotfr.weather.presentation.utils.VerticalMarginItemDecoration
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
        adapter.attachDelegate(
            object : FavoritePlacesDelegate {
                override fun placeClicked(placeId: Long) {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        "placeId",
                        placeId.toString()
                    )
                    findNavController().popBackStack()
                }
            }
        )
        binding.fragmentFavoritePlacesRv.adapter = adapter
        binding.fragmentFavoritePlacesRv.layoutManager = layoutManager
        binding.fragmentFavoritePlacesRv.addItemDecoration(
            VerticalMarginItemDecoration(spaceSize = resources.getDimensionPixelSize(R.dimen.medium_spacing))
        )

        binding.fragmentFavoritePlacesToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.fragment_favorite_places_toolbar_search -> {
                    val action = FavoritePlacesFragmentDirections
                        .actionFavoritePlacesFragmentToSearchPlacesFragment()
                    findNavController().navigate(action)
                    true
                }
                else -> false
            }
        }

        binding.fragmentFavoritePlacesToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
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
