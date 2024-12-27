package com.souqApp.data.common.utlis

import com.souqApp.infra.utils.SharedPrefs
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject

class RequestInterceptor(private val sharedPrefs: SharedPrefs) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sharedPrefs.getUserInfo()?.token

        val newRequest = chain.request()
            .newBuilder()
            .addHeader("Accept-Language", sharedPrefs.getLanguage())
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer $token")
            .build()

        val response = chain.proceed(newRequest)
        val responseBodyString = response.body.string()

        val jsonResponse = JSONObject(responseBodyString)
        if (jsonResponse.has("errors") && jsonResponse.get("errors") is JSONObject) {
            val errors = jsonResponse.getJSONObject("errors")
            if (errors.length() == 0) {
                jsonResponse.put("errors", JSONArray())
            }
        }

        val modifiedResponseBody = ResponseBody.create(
            "application/json".toMediaType(),
            jsonResponse.toString()
        )

        return response.newBuilder()
            .body(modifiedResponseBody)
            .apply { if (response.code != 200) code(200) }
            .build()
    }
}