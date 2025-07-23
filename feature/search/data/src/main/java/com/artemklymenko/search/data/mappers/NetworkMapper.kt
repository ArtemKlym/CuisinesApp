package com.artemklymenko.search.data.mappers

import com.artemklymenko.common.utils.NetworkError
import com.artemklymenko.common.utils.NetworkResult
import retrofit2.Response

fun <T> Response<T>.toNetworkResult(): NetworkResult<T> {
    return if (isSuccessful) {
        val body = body()
        if (body != null) {
            NetworkResult.Success(body)
        } else {
            NetworkResult.Error(NetworkError.SERIALIZATION_ERROR)
        }
    } else {
        when (code()) {
            408 -> NetworkResult.Error(NetworkError.REQUEST_TIMEOUT)
            429 -> NetworkResult.Error(NetworkError.TOO_MANY_REQUESTS)
            in 500..599 -> NetworkResult.Error(NetworkError.SERVER_ERROR)
            else -> NetworkResult.Error(NetworkError.UNKNOWN)
        }
    }
}