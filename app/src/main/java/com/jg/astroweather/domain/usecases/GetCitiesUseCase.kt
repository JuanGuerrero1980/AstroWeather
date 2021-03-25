package com.jg.astroweather.domain.usecases

import com.jg.astroweather.common.AWResult
import com.jg.astroweather.common.SafeFunctionExecutor
import com.jg.astroweather.domain.entities.City
import com.jg.astroweather.domain.repositories.CityRepository

class GetCitiesUseCase(
    private val cityRepository: CityRepository
) : BaseUseCase() {

    suspend operator fun invoke() : List<City> {
        return  cityRepository.getAllCities()
    }
}