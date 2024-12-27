package com.souqApp.domain.main.home.usecase

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.CheckUpdateResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.home.entity.CheckUpdateEntity
import com.souqApp.domain.main.home.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckUpdateUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    suspend fun execute(): Flow<BaseResult<CheckUpdateEntity, WrappedResponse<CheckUpdateResponse>>> {
        return flow {
            val response = homeRepository.checkUpdate()
            if (response.status) {
                emit(BaseResult.Success(response.data.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

}