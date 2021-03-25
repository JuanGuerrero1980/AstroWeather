package com.jg.astroweather.data.repositories

import com.jg.astroweather.data.datasources.remote.RemoteDataSource
import com.jg.astroweather.data.models.WeatherResponse
import com.jg.astroweather.domain.entities.*
import com.jg.astroweather.domain.repositories.WeatherRepository

class WeatherRepositoryImpl(private var remoteDataSource: RemoteDataSource): WeatherRepository {

    override suspend fun getWeatherByCityId(cityId: Int): CurrentWeather {
        val wr = remoteDataSource.getWeather(cityId)
        return convertEntity(wr)
    }

    override suspend fun getWeatherByLocation(lat: Number, lon: Number): CurrentWeather {
        val wr = remoteDataSource.getLocationWeather(lat, lon)
        return convertEntity(wr)
    }

    private fun convertEntity(wr: WeatherResponse?): CurrentWeather {
        val wl = mutableListOf<Weather>()
        wr?.weather.let {
            for (w in wr?.weather!!) {
                wl.add(Weather(w.id, w.main, w.description, w.icon, "http://openweathermap.org/img/wn/${w.icon}@2x.png"))
            }
        }

        return CurrentWeather(Coord(wr?.coord?.lat, wr?.coord?.lon), wl, wr?.base, Main(wr?.main?.temp, wr?.main?.pressure, wr?.main?.humidity, wr?.main?.temp_min, wr?.main?.temp_max),
                wr?.visibility, Wind(wr?.wind?.speed, wr?.wind?.deg), Clouds(wr?.clouds?.all), wr?.dt, Sys(wr?.sys?.type, wr?.sys?.id, wr?.sys?.message, wr?.sys?.country, wr?.sys?.sunrise, wr?.sys?.sunset),
                wr?.id, wr?.name, wr?.cod)
    }
}