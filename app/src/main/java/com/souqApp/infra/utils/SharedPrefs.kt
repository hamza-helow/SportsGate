package com.souqApp.infra.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.souqApp.BuildConfig
import com.souqApp.domain.common.entity.UserEntity
import java.util.Locale

@Suppress("UNCHECKED_CAST")
class SharedPrefs(context: Context) {

    companion object {
        private const val PREF = BuildConfig.APPLICATION_ID
        private const val PREF_USER_INFO = "user_info"
        private const val LANG = "lang_app"
        private const val FIREBASE_TOKEN = "firebase_token"
        private const val LAST_CART_UPDATE_TIME_STAMP = "last_cart_update_time_stamp"
        private const val LAST_WISH_LIST_UPDATE_TIME_STAMP = "last_wish_list_update_time_stamp"
    }

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)


    fun firebaseToken(token: String) {
        put(FIREBASE_TOKEN, token)
    }

    fun firebaseToken(): String {
        return get(FIREBASE_TOKEN, String::class.java)
    }

    fun setLanguage(code: String) {
        put(LANG, code)
    }

    fun getLanguage(): String {
        val lang = get(LANG, String::class.java)
        if (lang.isEmpty())
            return Locale.ENGLISH.language
        return lang
    }

    fun setLastCartUpdateTimeStamp(code: Long) {
        put(LAST_CART_UPDATE_TIME_STAMP, code)
    }

    fun getLastCartUpdateTimeStamp(): Long {
        return get(LAST_CART_UPDATE_TIME_STAMP, Long::class.java)
    }

    fun setLastWishListUpdateTimeStamp(code: Long) {
        put(LAST_WISH_LIST_UPDATE_TIME_STAMP, code)
    }

    fun getLastWishListUpdateTimeStamp(): Long {
        return get(LAST_WISH_LIST_UPDATE_TIME_STAMP, Long::class.java)
    }


    fun saveUserInfo(user: UserEntity) {
        val jsonString = Gson().toJson(user, UserEntity::class.java) ?: ""
        put(PREF_USER_INFO, jsonString)
    }

    fun getUserInfo(): UserEntity? {
        val result = get(PREF_USER_INFO, String::class.java)

        if (result.isEmpty())
            return null

        return Gson().fromJson(result, UserEntity::class.java)
    }

    private fun clearUserInfo() {
        put(PREF_USER_INFO, "")
    }

    fun isLogin(): Boolean {
        return getUserInfo()?.token.isNullOrEmpty().not()
    }

    fun logout() {
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