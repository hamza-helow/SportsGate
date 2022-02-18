package com.souqApp.domain.settings

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.ContentEntity
import com.souqApp.data.settings.remote.dto.SettingsEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow


interface SettingsRepository {

    suspend fun termsAndConditions(): Flow<BaseResult<ContentEntity, WrappedResponse<ContentEntity>>>

    suspend fun aboutUs(): Flow<BaseResult<ContentEntity, WrappedResponse<ContentEntity>>>

    suspend fun getSettings(): Flow<BaseResult<SettingsEntity, WrappedResponse<SettingsEntity>>>
}