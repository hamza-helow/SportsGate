package com.souqApp.domain.main.home

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.HomeResponse
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun home(): Flow<BaseResult<HomeEntity, WrappedResponse<HomeResponse>>>
}