package com.souqApp.domain.auth.usecase

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.auth.dto.LoginRequest
import com.souqApp.domain.auth.AuthRepository
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend fun invoke(loginRequest: LoginRequest): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>> {
        return flow {
            val response = authRepository.login(loginRequest)
            if (response.status) {
                val body = response.data
                emit(BaseResult.Success(body.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

}