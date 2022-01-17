package com.souqApp.data.common.utlis

import android.util.Log
import com.souqApp.infra.utils.SharedPrefs
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.RequestBody

class RequestInterceptor constructor(private val sharedPrefs: SharedPrefs) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPrefs.getToken()

        Log.e("ERe", "token :$token")

        val newRequest = chain.request()
            .newBuilder()
            .addHeader("Accept-Language", "en")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer $token").build()
        return chain.proceed(newRequest)
    }
}