package com.jg.astroweather.domain.errors

import java.io.IOException

class ErrorHandlerImpl : ErrorHandler {
    override fun mapError(throwable: Throwable): ErrorEntity {
        return when (throwable) {
            is IOException -> ErrorEntity.NetworkError
            is ErrorEntity -> throwable
            else -> ErrorEntity.UnknownError
        }
    }

    /**
     * Tracks error to the different tracking systems.
     */
    override fun trackError(throwable: Throwable) {

    }
}