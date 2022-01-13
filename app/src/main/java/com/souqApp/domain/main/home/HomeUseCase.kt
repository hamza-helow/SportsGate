package com.souqApp.domain.main.home

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.HomeResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.home.entity.HomeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    suspend fun home(): Flow<BaseResult<HomeEntity, WrappedResponse<HomeResponse>>> {
        return homeRepository.home()
    }
}