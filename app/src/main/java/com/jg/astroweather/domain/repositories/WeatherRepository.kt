package com.jg.astroweather.domain.repositories

import com.jg.astroweather.domain.entities.CurrentWeather

interface WeatherRepository {

    suspend fun getWeatherByCityId(cityId: Int) : CurrentWeather

    suspend fun getWeatherByLocation(lat: Number, lon: Number) : CurrentWeather
}