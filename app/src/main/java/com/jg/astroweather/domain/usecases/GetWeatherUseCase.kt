package com.jg.astroweather.domain.usecases

import com.jg.astroweather.common.AWResult
import com.jg.astroweather.common.SafeFunctionExecutor
import com.jg.astroweather.domain.entities.CurrentWeather
import com.jg.astroweather.domain.repositories.WeatherRepository

class GetWeatherUseCase(private val weatherRepository: WeatherRepository,
                        private val safeFunctionExecutor: SafeFunctionExecutor,
                        private var cityId: Int
) : BaseUseCase() {

    suspend operator fun invoke(): AWResult<CurrentWeather> {

        return safeFunctionExecutor.executeSafeFunction {
           weatherRepository.getWeatherByCityId(cityId)
        }

    }
}