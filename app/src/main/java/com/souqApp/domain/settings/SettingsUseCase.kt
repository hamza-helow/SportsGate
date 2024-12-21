package com.souqApp.domain.settings

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.SettingsEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend fun invoke(): Flow<BaseResult<SettingsEntity, WrappedResponse<SettingsEntity>>> {
        return settingsRepository.getSettings()
    }
}