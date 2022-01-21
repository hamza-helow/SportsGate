package com.souqApp.infra.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.souqApp.BuildConfig
import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.domain.common.entity.UserEntity
import org.json.JSONObject


class SharedPrefs(context: Context) {

    companion object {
        private const val PREF = BuildConfig.APP_NAME
        private const val PREF_TOKEN = "user_token"
        private const val PREF_USER_INFO = "user_info"
    }

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)


    fun saveToken(token: String) {
        put(PREF_TOKEN, token)
    }

    fun saveUserInfo(userResponse: UserResponse) {
        val jsonString = Gson().toJson(userResponse, UserResponse::class.java) ?: ""
        put(PREF_USER_INFO, jsonString)
    }

    fun getUserInfo(): UserEntity? {
        val result = get(PREF_USER_INFO, String::class.java)

        if (result.isEmpty())
            return null

        return Gson().fromJson(result, UserResponse::class.java).toEntity()
    }

    fun clearUserInfo() {
        put(PREF_USER_INFO, "")
    }

    fun isLogin(): Boolean {
        return getToken().isNotEmpty()
    }

    fun getToken() = get(PREF_TOKEN, String::class.java)

    fun clear() {
        saveToken("")
        clearUserInfo()
    }

    private fun <T> get(key: String, clazz: Class<T>) =
        when (clazz) {
            String::class.java -> sharedPref.getString(key, "")
            Boolean::class.java -> sharedPref.getBoolean(key, false)
            Float::class.java -> sharedPref.getFloat(key, 0.0f)
            Double::class.java -> sharedPref.getFloat(key, 0.0f)
            Int::class.java -> sharedPref.getInt(key, 0)
            Long::class.java -> sharedPref.getLong(key, 0)
            else -> null
        } as T

    private fun <T> put(key: String, data: T) {
        val editor = sharedPref.edit()

        when (data) {
            is String -> editor.putString(key, data)
            is Boolean -> editor.putBoolean(key, data)
            is Float -> editor.putFloat(key, data)
            is Double -> editor.putFloat(key, data.toFloat())
            is Int -> editor.putInt(key, data)

        }

        editor.apply()
    }

}