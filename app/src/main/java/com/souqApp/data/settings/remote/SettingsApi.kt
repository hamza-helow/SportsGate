package com.souqApp.data.settings.remote

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.ContentEntity
import com.souqApp.data.settings.remote.dto.PageDetailsEntity
import com.souqApp.data.settings.remote.dto.PageEntity
import com.souqApp.data.settings.remote.dto.SettingsEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface SettingsApi {

    @GET("v2/settings/termsAndConditions")
    suspend fun termsAndConditions(): WrappedResponse<ContentEntity>

    @GET("v2/settings/aboutUs")
    suspend fun aboutUs(): WrappedResponse<ContentEntity>

    @GET("v2/settings")
    suspend fun getSettings(): WrappedResponse<SettingsEntity>

    @GET("v2/pages")
    suspend fun getPages(): WrappedListResponse<PageEntity>

    @GET("v2/pageDetails")
    suspend fun getPageDetails(@Query("id") pageId: Int?): WrappedResponse<PageDetailsEntity>
}