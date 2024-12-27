package com.souqApp.domain.settings

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.ContactUsRequest
import com.souqApp.data.settings.remote.dto.PageDetailsEntity
import com.souqApp.data.settings.remote.dto.PageEntity
import com.souqApp.data.settings.remote.dto.SettingsEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow


interface SettingsRepository {

    suspend fun getSettings(): WrappedResponse<SettingsEntity>

    suspend fun getPages(): WrappedListResponse<PageEntity>

    suspend fun getPageDetails(pageId:Int?): WrappedResponse<PageDetailsEntity>

    suspend fun sendContactUs(contactUsRequest: ContactUsRequest): WrappedResponse<Nothing>
}