package com.jg.astroweather.presentation.cities

import com.jg.astroweather.domain.entities.City

sealed class CitiesListUIState {
    object Loading : CitiesListUIState()
    data class Initial(val cities: List<City>) : CitiesListUIState()
    data class Error(val errorMessage: String) : CitiesListUIState()
    data class CitySelected(val city: City) : CitiesListUIState()
}