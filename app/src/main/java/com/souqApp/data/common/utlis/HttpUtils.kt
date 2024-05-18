package com.souqApp.data.common.utlis

import com.google.gson.Gson
import retrofit2.HttpException

inline fun <reified T> handleApi(call: () -> T): T {
    return try {
        call()
    } catch (e: HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        parseResponse(errorBody.orEmpty())
    }
}

inline fun <reified T> parseResponse(errorBody: String): T {
    return Gson().fromJson(
        errorBody,
        T::class.java
    )
}