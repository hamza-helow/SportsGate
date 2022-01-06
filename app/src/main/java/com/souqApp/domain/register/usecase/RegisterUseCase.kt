package com.souqApp.domain.register.usecase

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.register.remote.dto.ActiveAccountRequest
import com.souqApp.data.register.remote.dto.RegisterRequest
import com.souqApp.data.register.remote.dto.RegisterResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.register.RegisterRepository
import com.souqApp.domain.register.entity.RegisterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val registerRepository: RegisterRepository) {

    suspend fun invokeRegister(registerRequest: RegisterRequest): Flow<BaseResult<RegisterEntity, WrappedResponse<RegisterResponse>>> {
        return registerRepository.register(registerRequest)
    }

    suspend fun invokeActiveAccount(activeAccountRequest: ActiveAccountRequest): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>> {
        return registerRepository.activeAccount(activeAccountRequest)
    }


}