package com.jg.astroweather.data.repositories

import com.jg.astroweather.domain.entities.City
import com.jg.astroweather.domain.repositories.CityRepository

class CityRepositoryImpl : CityRepository {

    override suspend fun getAllCities(): List<City> {
        val list = mutableListOf<City>()
        list.add(City(3441575, "Montevideo"))
        list.add(City(3846616, "Londres"))
        list.add(City(3871865, "San Pablo"))
        list.add(City(3435907, "Buenos Aires"))
        list.add(City(2867714, "Munich"))
        return list
    }



}
