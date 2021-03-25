package com.jg.astroweather.domain.usecases

import com.jg.astroweather.common.AWResult
import com.jg.astroweather.common.SafeFunctionExecutor
import com.jg.astroweather.domain.entities.CurrentWeather
import com.jg.astroweather.domain.repositories.WeatherRepository

class GetLocationWeatherUseCase (private val weatherRepository: WeatherRepository,
                                 private val safeFunctionExecutor: SafeFunctionExecutor,
                                 private var lat: Number,
                                 private var lon: Number
) : BaseUseCase() {

    suspend operator fun invoke(): AWResult<CurrentWeather> {
        return safeFunctionExecutor.executeSafeFunction {
            weatherRepository.getWeatherByLocation(lat, lon)
        }

    }
}