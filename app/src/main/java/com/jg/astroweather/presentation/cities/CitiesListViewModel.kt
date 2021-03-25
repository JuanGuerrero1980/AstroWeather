package com.jg.astroweather.presentation.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jg.astroweather.domain.entities.City
import com.jg.astroweather.domain.repositories.CityRepository
import com.jg.astroweather.domain.usecases.GetCitiesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CitiesListViewModel(private val cityRepository: CityRepository): ViewModel() {

    private val mState = MutableStateFlow<CitiesListUIState>(CitiesListUIState.Loading)
    val state: StateFlow<CitiesListUIState> = mState

    fun init()  = viewModelScope.launch {
        mState.value = CitiesListUIState.Loading
        val getCitiesUseCase = GetCitiesUseCase(cityRepository)
        mState.value = CitiesListUIState.Initial(getCitiesUseCase())
    }

    fun onCityTapped(city: City) = viewModelScope.launch {

        mState.value = CitiesListUIState.CitySelected(city)
    }
}

class CitiesListViewModelFactory(private val cityRepository: CityRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CityRepository::class.java).newInstance(cityRepository)
    }

}