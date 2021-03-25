package com.jg.astroweather.common

import com.jg.astroweather.domain.errors.ErrorEntity

sealed class AWResult<T> {
    class Success<T>(val result: T) : AWResult<T>()
    class Error<T>(val error: ErrorEntity) : AWResult<T>()
}