package com.jg.astroweather.presentation.weather

import com.jg.astroweather.domain.entities.CurrentWeather

sealed class WeatherUIState {
    object Loading : WeatherUIState()
    data class Success(val currentWeather: CurrentWeather) : WeatherUIState()
    object Initial : WeatherUIState()
    data class Error(val errorMessage: String) : WeatherUIState()
}