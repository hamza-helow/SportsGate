package com.souqApp.data.login.repository

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.login.remote.api.LoginApi
import com.souqApp.data.login.remote.dto.LoginRequest
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.login.LoginRepository
import com.souqApp.domain.common.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val loginApi: LoginApi) : LoginRepository {
    override suspend fun login(loginRequest: LoginRequest): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>> {
        return flow {
            val response = loginApi.login(loginRequest)
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                val body = response.body()!!.data!!
                val loginEntity = UserEntity(
                    body.id,
                    body.name,
                    body.email,
                    body.phone,
                    body.image,
                    body.verified,
                    body.token
                )
                emit(BaseResult.Success(loginEntity))
            } else {
                emit(BaseResult.Errors(response.body()!!))
            }
        }
    }
}