package com.jg.astroweather.domain.errors

interface ErrorHandler {
    fun mapError(throwable: Throwable): ErrorEntity

    fun trackError(throwable: Throwable)
}