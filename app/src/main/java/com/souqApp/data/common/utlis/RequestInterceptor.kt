package com.souqApp.data.common.utlis

import com.souqApp.infra.utils.SharedPrefs
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor constructor(private val sharedPrefs: SharedPrefs) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPrefs.getToken()

        val lang = if (sharedPrefs.getLanguage() == "En") "en" else "ar"

        val newRequest = chain.request()
            .newBuilder()
            .addHeader("Accept-Language", lang)
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer $token").build()
        return chain.proceed(newRequest)
    }
}