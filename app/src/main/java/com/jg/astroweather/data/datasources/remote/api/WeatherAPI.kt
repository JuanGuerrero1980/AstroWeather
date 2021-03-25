package com.jg.astroweather.data.datasources.remote.api

import com.jg.astroweather.data.models.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// API key e185473345a7c16de9c04962a3f2947c

interface WeatherAPI {

    @GET("/data/2.5/weather")
    suspend fun getWeather(@Query("id") id: Number, @Query("units") units: String = "metric", @Query("APPID") appID: String = "e185473345a7c16de9c04962a3f2947c") : Response<WeatherResponse>

    @GET("/data/2.5/weather")
    suspend fun getWeatherByCoordinates(@Query("lat") lat: Number, @Query("lon") lon: Number, @Query("units") units: String = "metric", @Query("APPID") appID: String = "e185473345a7c16de9c04962a3f2947c") : Response<WeatherResponse>
}