package com.souqApp.domain.main.home.usecase

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.home.remote.dto.HomeResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.home.entity.HomeEntity
import com.souqApp.domain.main.home.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetHomeUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    suspend fun invoke(): Flow<BaseResult<HomeEntity, WrappedResponse<HomeResponse>>> {
        return  flow {
            val response = homeRepository.getHome()
            val isSuccessful = response.status

            if (isSuccessful) {
                emit(BaseResult.Success(response.data.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}