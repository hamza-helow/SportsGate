package com.souqApp.domain.settings.usecase

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.SettingsEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SettingsUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend fun invoke(): Flow<BaseResult<SettingsEntity, WrappedResponse<SettingsEntity>>> {
        return flow {
            val response = settingsRepository.getSettings()
            if (response.status) {
                emit(BaseResult.Success(response.data))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}