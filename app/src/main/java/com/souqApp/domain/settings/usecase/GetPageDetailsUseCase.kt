package com.souqApp.domain.settings.usecase

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.PageDetailsEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPageDetailsUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend fun invoke(pageId: Int?): Flow<BaseResult<PageDetailsEntity, WrappedResponse<PageDetailsEntity>>> {
        return flow {
            val response = settingsRepository.getPageDetails(pageId)
            if (response.status) {
                emit(BaseResult.Success(response.data))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}