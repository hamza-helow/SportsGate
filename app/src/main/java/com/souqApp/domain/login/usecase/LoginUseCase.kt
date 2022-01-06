package com.souqApp.domain.login.usecase

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.login.remote.dto.LoginRequest
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.login.LoginRepository
import com.souqApp.domain.common.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun invoke(loginRequest: LoginRequest): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>> {
        return loginRepository.login(loginRequest)
    }
}