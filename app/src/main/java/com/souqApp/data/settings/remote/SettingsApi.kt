package com.souqApp.data.settings.remote

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.ContentEntity
import com.souqApp.data.settings.remote.dto.SettingsEntity
import retrofit2.Response
import retrofit2.http.GET

interface SettingsApi {

    @GET("v1/settings/termsAndConditions")
    suspend fun termsAndConditions(): Response<WrappedResponse<ContentEntity>>

    @GET("v1/settings/aboutUs")
    suspend fun aboutUs(): Response<WrappedResponse<ContentEntity>>

    @GET("v1/users/getSettings")
    suspend fun getSettings(): Response<WrappedResponse<SettingsEntity>>
}