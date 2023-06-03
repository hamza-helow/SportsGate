package com.souqApp.domain.main.home

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.CheckUpdateResponse
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckUpdateUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    suspend fun execute(): Flow<BaseResult<CheckUpdateEntity, WrappedResponse<CheckUpdateResponse>>> {
        return homeRepository.checkUpdate()
    }

}