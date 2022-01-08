package com.souqApp.domain.register.usecase

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.register.remote.dto.RegisterRequest
import com.souqApp.data.register.remote.dto.RegisterResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.register.RegisterRepository
import com.souqApp.domain.register.entity.RegisterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val registerRepository: RegisterRepository) {

    suspend fun invokeRegister(registerRequest: RegisterRequest): Flow<BaseResult<RegisterEntity, WrappedResponse<RegisterResponse>>> {
        return registerRepository.register(registerRequest)
    }

}