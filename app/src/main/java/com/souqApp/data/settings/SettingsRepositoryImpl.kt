package com.souqApp.data.settings

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.SettingsApi
import com.souqApp.data.settings.remote.dto.ContactUsRequest
import com.souqApp.data.settings.remote.dto.PageDetailsEntity
import com.souqApp.data.settings.remote.dto.PageEntity
import com.souqApp.data.settings.remote.dto.SettingsEntity
import com.souqApp.domain.settings.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(private val settingsApi: SettingsApi) :
    SettingsRepository {

    override suspend fun getSettings(): WrappedResponse<SettingsEntity> {
        return settingsApi.getSettings()
    }

    override suspend fun getPages(): WrappedListResponse<PageEntity> {
        return settingsApi.getPages()
    }

    override suspend fun getPageDetails(pageId: Int?):WrappedResponse<PageDetailsEntity> {
        return settingsApi.getPageDetails(pageId)
    }

    override suspend fun sendContactUs(contactUsRequest: ContactUsRequest): WrappedResponse<Nothing> {
        return settingsApi.sendContactUs(contactUsRequest)
    }

}