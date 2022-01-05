package com.souqApp.data.login.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.login.remote.api.LoginApi
import com.souqApp.data.login.remote.dto.LoginRequest
import com.souqApp.data.login.remote.dto.LoginResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.login.LoginRepository
import com.souqApp.domain.login.entity.LoginEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val loginApi: LoginApi) : LoginRepository {
    override suspend fun login(loginRequest: LoginRequest): Flow<BaseResult<LoginEntity, WrappedResponse<LoginResponse>>> {
        return flow {
            val response = loginApi.login(loginRequest)
            if (response.isSuccessful) {
                val body = response.body()!!.data!!
                val loginEntity = LoginEntity(
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

                val type = object : TypeToken<WrappedResponse<LoginResponse>>() {}.type
                val error: WrappedResponse<LoginResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                emit(BaseResult.Errors(error))
            }
        }
    }
}