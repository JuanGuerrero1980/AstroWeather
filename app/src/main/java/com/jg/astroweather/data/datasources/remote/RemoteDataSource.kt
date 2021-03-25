package com.jg.astroweather.data.datasources.remote

import com.jg.astroweather.data.datasources.remote.api.WeatherAPI
import com.jg.astroweather.data.models.WeatherResponse
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RemoteDataSource {

    private var weatherAPI: WeatherAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //identify the endpoints
        weatherAPI = retrofit.create(WeatherAPI::class.java)
    }

    suspend fun getWeather(cityId: Int): WeatherResponse? {

        val weatherCall = weatherAPI.getWeather(cityId)
        if(weatherCall.isSuccessful){
            return weatherCall.body()
        }else{
            throw Exception(weatherCall.errorBody().toString())
        }

    }

    suspend fun getLocationWeather(lat: Number, lon: Number): WeatherResponse? {

        val weatherCall = weatherAPI.getWeatherByCoordinates(lat, lon)
        if(weatherCall.isSuccessful){
            return weatherCall.body()
        }else{
            throw Exception(weatherCall.errorBody().toString())
        }

    }

    /**
     * Executes asynchronous Retrofit call in synchronous manner
     * @param call call to be executed
     * @return object of type T or null in case any exception occurs
     */
    suspend fun <T> executeSync(call: Call<T>): T? {
        return callSuspendWrapper {callback: Callback<T> ->
            call.enqueue(callback)
        }
    }

    private suspend fun <T> callSuspendWrapper(block: (Callback<T>) -> Unit): T? {
        return suspendCancellableCoroutine<T?> { cont: CancellableContinuation<T?> ->
            block(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    cont.resume(response.body())
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    cont.resumeWithException(t)
                }
            })
        }
    }
}