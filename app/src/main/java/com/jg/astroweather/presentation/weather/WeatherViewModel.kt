package com.jg.astroweather.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jg.astroweather.common.AWResult.*
import com.jg.astroweather.common.SafeFunctionExecutor
import com.jg.astroweather.domain.repositories.WeatherRepository
import com.jg.astroweather.domain.usecases.GetLocationWeatherUseCase
import com.jg.astroweather.domain.usecases.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherRepository: WeatherRepository, private val safeFunctionExecutor: SafeFunctionExecutor): ViewModel() {

    private val mState = MutableStateFlow<WeatherUIState>(WeatherUIState.Initial)
    val state: StateFlow<WeatherUIState> = mState

    fun init() {
        mState.value = WeatherUIState.Initial
    }

    fun onSearchButtonTapped(cityId: Int) = viewModelScope.launch {
        mState.value = WeatherUIState.Loading

        val getWeatherUseCase = GetWeatherUseCase(weatherRepository, safeFunctionExecutor, cityId)
        when (val response = getWeatherUseCase() ){
            is Success-> mState.value = WeatherUIState.Success(response.result)
            is Error -> mState.value = WeatherUIState.Error(response.error.message)
        }
    }

    fun onSearchWeatherLocation(lat: Number, lon: Number) = viewModelScope.launch {
        mState.value = WeatherUIState.Loading

        val getLocationWeatherUseCase = GetLocationWeatherUseCase(weatherRepository, safeFunctionExecutor, lat, lon)
        when (val response = getLocationWeatherUseCase() ){
            is Success-> mState.value = WeatherUIState.Success(response.result)
            is Error -> mState.value = WeatherUIState.Error(response.error.message)
        }
    }

    fun onError(message: String) {
        mState.value = WeatherUIState.Error(message)
    }

    fun gettingCurrentLocation() {
        mState.value = WeatherUIState.Loading
    }
}

class WeatherViewModelFactory(private val weatherRepository: WeatherRepository, private val safeFunctionExecutor: SafeFunctionExecutor) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(WeatherRepository::class.java, SafeFunctionExecutor::class.java).newInstance(weatherRepository, safeFunctionExecutor)
    }

}