package com.jg.astroweather.common

interface SafeFunctionExecutor {

    suspend fun <T> executeSafeFunction(function: suspend () -> T): AWResult<T>

}