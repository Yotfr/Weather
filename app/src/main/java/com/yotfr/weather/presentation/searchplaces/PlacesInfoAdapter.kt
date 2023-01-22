package com.yotfr.weather.presentation.searchplaces

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yotfr.weather.databinding.ItemSearchedPlaceInfoBinding
import com.yotfr.weather.domain.model.PlaceInfo

interface SearchedPlacesDelegate {
    fun addPlaceClicked(place: PlaceInfo)
    fun placeClicked(place: PlaceInfo)
}

class LocationInfoDiffUtilCallback : DiffUtil.ItemCallback<PlaceInfo>() {
    override fun areItemsTheSame(oldItem: PlaceInfo, newItem: PlaceInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PlaceInfo, newItem: PlaceInfo): Boolean {
        return oldItem == newItem
    }
}

class LocationInfoAdapter : ListAdapter<PlaceInfo, LocationInfoAdapter.LocationInfoViewHolder>(
    LocationInfoDiffUtilCallback()
) {

    private var delegate: SearchedPlacesDelegate? = null

    fun attachDelegate(delegate: SearchedPlacesDelegate) {
        this.delegate = delegate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationInfoViewHolder {
        return LocationInfoViewHolder(
            binding = ItemSearchedPlaceInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            delegate = delegate
        )
    }

    override fun onBindViewHolder(holder: LocationInfoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LocationInfoViewHolder(
        private val binding: ItemSearchedPlaceInfoBinding,
        private val delegate: SearchedPlacesDelegate?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(placeInfo: PlaceInfo) {
            binding.apply {
                itemLocationInfoTvPlaceName.text = placeInfo.placeName
                itemLocationInfoTvCountryName.text = placeInfo.countryName
                btnAddFavorite.setOnClickListener {
                    delegate?.addPlaceClicked(place = placeInfo)
                }
            }
        }
    }
}
