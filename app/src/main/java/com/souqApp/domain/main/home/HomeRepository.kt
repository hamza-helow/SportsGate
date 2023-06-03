package com.souqApp.domain.main.home

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.CheckUpdateResponse
import com.souqApp.data.main.home.remote.dto.HomeResponse
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun home(): Flow<BaseResult<HomeEntity, WrappedResponse<HomeResponse>>>

    suspend fun checkUpdate(): Flow<BaseResult<CheckUpdateEntity, WrappedResponse<CheckUpdateResponse>>>

}