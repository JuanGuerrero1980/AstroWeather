package com.jg.astroweather.common

import android.util.Log
import com.jg.astroweather.domain.errors.ErrorHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SafeFunctionExecutorImpl(private val errorHandler: ErrorHandler) : SafeFunctionExecutor {

    companion object {
        val TAG: String = SafeFunctionExecutor::class.java.simpleName
    }

    override suspend fun <T> executeSafeFunction(function: suspend () -> T): AWResult<T> {
        return try {
            withContext(Dispatchers.IO) {
                AWResult.Success(function())
            }
        } catch (throwable: Throwable) {
            Log.e(TAG, throwable.message ?: "Error throwable message is null")
            errorHandler.trackError(throwable)
            AWResult.Error(errorHandler.mapError(throwable))
        }
    }


}