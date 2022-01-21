package com.souqApp.domain.register

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.register.remote.dto.RegisterRequest
import com.souqApp.data.common.remote.dto.TokenResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.TokenEntity
import kotlinx.coroutines.flow.Flow

interface RegisterRepository {
    suspend fun register(registerRequest: RegisterRequest): Flow<BaseResult<TokenEntity, WrappedResponse<TokenResponse>>>

}