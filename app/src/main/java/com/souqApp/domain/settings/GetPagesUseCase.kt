package com.souqApp.domain.settings

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.settings.remote.dto.PageEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagesUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend fun invoke(): Flow<BaseResult<List<PageEntity>, WrappedListResponse<PageEntity>>> {
        return settingsRepository.getPages()
    }
}