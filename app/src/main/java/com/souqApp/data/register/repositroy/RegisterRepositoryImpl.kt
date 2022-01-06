package com.souqApp.data.register.repositroy

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.register.remote.api.RegisterApi
import com.souqApp.data.register.remote.dto.ActiveAccountRequest
import com.souqApp.data.register.remote.dto.RegisterRequest
import com.souqApp.data.register.remote.dto.RegisterResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.register.RegisterRepository
import com.souqApp.domain.register.entity.RegisterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(private val registerApi: RegisterApi) :
    RegisterRepository {
    override suspend fun register(registerRequest: RegisterRequest): Flow<BaseResult<RegisterEntity, WrappedResponse<RegisterResponse>>> {

        return flow {

            val response = registerApi.register(registerRequest)

            if (response.isSuccessful) {
                val body = response.body()!!.data!!
                val registerEntity = RegisterEntity(body.token)
                emit(BaseResult.Success(registerEntity))
            } else {
                val type = object : TypeToken<WrappedResponse<RegisterResponse>>() {}.type
                val error: WrappedResponse<RegisterResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                emit(BaseResult.Errors(error))
            }

        }
    }

    override suspend fun activeAccount(activeAccountRequest: ActiveAccountRequest): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>> {
        return flow {

            val response = registerApi.activeAccount(activeAccountRequest)

            if (response.isSuccessful) {

                val body = response.body()!!.data!!
                val userEntity = UserEntity(
                    body.id,
                    body.name,
                    body.email,
                    body.phone,
                    body.image,
                    body.verified,
                    body.token
                )

                emit(BaseResult.Success(userEntity))
            } else {
                val type = object : TypeToken<WrappedResponse<UserResponse>>() {}.type
                val error: WrappedResponse<UserResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                emit(BaseResult.Errors(error))
            }
        }
    }
}