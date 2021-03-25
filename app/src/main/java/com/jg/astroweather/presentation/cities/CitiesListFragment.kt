package com.jg.astroweather.presentation.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jg.astroweather.data.repositories.CityRepositoryImpl
import com.jg.astroweather.databinding.FragmentCitiesListBinding
import com.jg.astroweather.domain.entities.City
import com.jg.astroweather.presentation.BaseFragment
import kotlinx.coroutines.flow.collect

class CitiesListFragment: BaseFragment() {

    private lateinit var binding: FragmentCitiesListBinding
    private lateinit var adapter: CitiesListAdapter
    private lateinit var viewModel: CitiesListViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentCitiesListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = CitiesListAdapter { city: City -> citiesListItemClicked(city) }
        binding.cityList.adapter = adapter
        binding.cityList.layoutManager = LinearLayoutManager(requireContext())
        val cityRepository = CityRepositoryImpl()
        val viewModelFactory = CitiesListViewModelFactory(cityRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CitiesListViewModel::class.java)
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect(::handleState)
        }
        binding.fabAddLocation.setOnClickListener{
            navigateToLocationWeather()
        }
        viewModel.init()
    }

    private fun handleState(state: CitiesListUIState){
        when (state){
            CitiesListUIState.Loading -> displayLoadingState()
            is CitiesListUIState.Initial -> displayInitialState(state.cities)
            is CitiesListUIState.Error -> displayError(state.errorMessage)
            is CitiesListUIState.CitySelected -> navigateToCityWeather(state.city)
        }
    }

    private fun navigateToCityWeather(city: City) {
        val direction = CitiesListFragmentDirections.actionCitiesListFragmentToWeatherFragment(city.id)
        findNavController().navigate(direction)
    }

    private fun citiesListItemClicked(city: City){
        viewModel.onCityTapped(city)
    }

    private fun displayLoadingState(){

    }

    private fun displayInitialState(list: List<City>){
        adapter.submitList(list)
    }

    private fun displayError(message: String){

    }

    private fun navigateToLocationWeather(){
        val direction = CitiesListFragmentDirections.actionCitiesListFragmentToWeatherFragment(0)
        findNavController().navigate(direction)
    }
}