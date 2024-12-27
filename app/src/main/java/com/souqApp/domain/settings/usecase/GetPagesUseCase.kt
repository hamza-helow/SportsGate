package com.souqApp.domain.settings.usecase

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.settings.remote.dto.PageEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPagesUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend fun invoke(): Flow<BaseResult<List<PageEntity>, WrappedListResponse<PageEntity>>> {
        return flow {
            val response = settingsRepository.getPages()
            if (response.status) {
                emit(BaseResult.Success(response.data.orEmpty()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}