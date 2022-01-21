package com.souqApp.domain.register.usecase

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.register.remote.dto.RegisterRequest
import com.souqApp.data.common.remote.dto.TokenResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.register.RegisterRepository
import com.souqApp.domain.common.entity.TokenEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val registerRepository: RegisterRepository) {

    suspend fun invokeRegister(registerRequest: RegisterRequest): Flow<BaseResult<TokenEntity, WrappedResponse<TokenResponse>>> {
        return registerRepository.register(registerRequest)
    }

}