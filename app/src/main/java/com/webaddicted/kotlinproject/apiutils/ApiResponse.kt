package com.webaddicted.kotlinproject.apiutils

import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.global.common.AppApplication
import java.net.SocketTimeoutException

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class ApiResponse<T>(val status: Status, val data: T?, val error: Throwable?) {

    var errorMessage: String? = null
    var errorType: Error? = null

    init {
        if (error == null && status == Status.ERROR) {
            errorType = Error.API_ERROR
        } else if (error is SocketTimeoutException) {
            errorType = Error.TIMEOUT_ERROR
            errorMessage = AppApplication.context.getString(R.string.network_error)
        } else {
            errorType = Error.SERVER_ERROR
            errorMessage = AppApplication.context.getString(R.string.internal_server_error)
        }
    }

    companion object {
        fun <T> loading(): ApiResponse<T> {
            return ApiResponse(
                Status.LOADING,
                null,
                null
            )
        }

        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(
                Status.SUCCESS,
                data,
                null
            )
        }

        fun <T> error(error: Throwable?): ApiResponse<T> {
            return ApiResponse(
                Status.ERROR,
                null,
                error
            )
        }
    }

    enum class Status {
        LOADING,
        SUCCESS,
        ERROR,
    }

    enum class Error {
        SERVER_ERROR,
        TIMEOUT_ERROR,
        API_ERROR

    }
}