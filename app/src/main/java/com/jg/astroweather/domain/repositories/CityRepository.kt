package com.jg.astroweather.domain.repositories

import com.jg.astroweather.domain.entities.City

interface CityRepository {

    suspend fun getAllCities() : List<City>

}