package com.souqApp.domain.settings

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.PageDetailsEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPageDetailsUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend fun invoke(pageId: Int?): Flow<BaseResult<PageDetailsEntity, WrappedResponse<PageDetailsEntity>>> {
        return settingsRepository.getPageDetails(pageId)
    }
}