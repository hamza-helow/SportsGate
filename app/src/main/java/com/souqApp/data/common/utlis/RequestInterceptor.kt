package com.souqApp.data.common.utlis

import com.souqApp.infra.utils.SharedPrefs
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.RequestBody




class RequestInterceptor constructor(private val sharedPrefs: SharedPrefs) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPrefs.getToken()
        val newRequest = chain.request()
            .newBuilder()
            .addHeader("Authorization", token).build()

        return chain.proceed(newRequest)
    }
}