package com.jg.astroweather.domain.errors

sealed class ErrorEntity(override val message: String) : Throwable(message) {

    object NetworkError: ErrorEntity("No Internet")
    object UnknownError : ErrorEntity("An error occurred processing your request")
}