package com.souqApp.data.main.home.remote

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.CheckUpdateResponse
import com.souqApp.data.main.home.remote.dto.HomeResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.home.CheckUpdateEntity
import com.souqApp.domain.main.home.HomeEntity
import com.souqApp.domain.main.home.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val homeApi: HomeApi) : HomeRepository {
    override suspend fun home(): Flow<BaseResult<HomeEntity, WrappedResponse<HomeResponse>>> {
        return flow {
            val response = homeApi.getHome()
            val isSuccessful = response.status

            if (isSuccessful) {
                emit(BaseResult.Success(response.data.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }


    override suspend fun checkUpdate(): Flow<BaseResult<CheckUpdateEntity, WrappedResponse<CheckUpdateResponse>>> {
        return flow {
            val response = homeApi.checkUpdate()
            if (response.status) {
                emit(BaseResult.Success(response.data.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}