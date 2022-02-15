package com.souqApp.domain.main.home

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.HomeEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    suspend fun home(): Flow<BaseResult<HomeEntity, WrappedResponse<HomeEntity>>> {
        return homeRepository.home()
    }
}