package com.jg.astroweather.presentation.cities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jg.astroweather.databinding.AdapterCitiesListBinding
import com.jg.astroweather.domain.entities.City

class CitiesListAdapter(
        private val itemClickListener: (City) -> Unit
) :
        ListAdapter<City, CitiesListAdapter.CitiesListViewHolder>(
                CitiesDiffUtil
        ) {
    inner class CitiesListViewHolder(val binding: AdapterCitiesListBinding) :
            RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClickListener(getItem(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesListViewHolder {
        return CitiesListViewHolder(AdapterCitiesListBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: CitiesListViewHolder, position: Int) {
        holder.binding.cityName.text = getItem(position).name
    }
}

object CitiesDiffUtil : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem == newItem
    }

}
