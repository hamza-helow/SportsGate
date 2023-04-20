package com.souqApp.data.register.repositroy

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.register.remote.api.RegisterApi
import com.souqApp.data.register.remote.dto.RegisterRequest
import com.souqApp.data.common.remote.dto.TokenResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.register.RegisterRepository
import com.souqApp.domain.common.entity.TokenEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(private val registerApi: RegisterApi) :
    RegisterRepository {
    override suspend fun register(registerRequest: RegisterRequest): Flow<BaseResult<TokenEntity, WrappedResponse<TokenResponse>>> {

        return flow {

            val response = registerApi.register(registerRequest)
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                val body = response.body()!!.data!!
                val registerEntity = TokenEntity(body.token)
                emit(BaseResult.Success(registerEntity))
            } else {
                emit(BaseResult.Errors(response.body()!!))
            }

        }
    }
}