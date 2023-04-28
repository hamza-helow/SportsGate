package com.souqApp.data.settings

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.SettingsApi
import com.souqApp.data.settings.remote.dto.ContentEntity
import com.souqApp.data.settings.remote.dto.SettingsEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(private val settingsApi: SettingsApi) :
    SettingsRepository {
    override suspend fun termsAndConditions(): Flow<BaseResult<ContentEntity, WrappedResponse<ContentEntity>>> {
        return flow {
            val response = settingsApi.termsAndConditions()
            if (response.status) {
                emit(BaseResult.Success(response.data))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

    override suspend fun aboutUs(): Flow<BaseResult<ContentEntity, WrappedResponse<ContentEntity>>> {
        return flow {
            val response = settingsApi.aboutUs()
            if (response.status) {
                emit(BaseResult.Success(response.data))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

    override suspend fun getSettings(): Flow<BaseResult<SettingsEntity, WrappedResponse<SettingsEntity>>> {
        return flow {
            val response = settingsApi.getSettings()
            if (response.status) {
                emit(BaseResult.Success(response.data))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}