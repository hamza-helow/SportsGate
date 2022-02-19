package com.souqApp.data.verifcation

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.VerificationApi
import com.souqApp.data.verifcation.remote.dto.ActiveAccountRequest
import com.souqApp.data.verifcation.remote.dto.CreateTokenResetPasswordEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.verifcation.VerificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VerificationRepositoryImpl @Inject constructor(private val verificationApi: VerificationApi) :
    VerificationRepository {
    override suspend fun activeAccount(activeAccountRequest: ActiveAccountRequest): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>> {
        return flow {
            val response = verificationApi.activeAccount(activeAccountRequest)
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                val body = response.body()!!.data!!
                val entity = UserEntity(
                    body.id,
                    body.name,
                    body.email,
                    body.phone,
                    body.image,
                    body.verified,
                    body.token
                )
                emit(BaseResult.Success(entity))
            } else {
                emit(BaseResult.Errors(response.body()!!))
            }

        }
    }

    override suspend fun createTokenResetPassword(
        phone: String,
        code: String
    ): Flow<BaseResult<CreateTokenResetPasswordEntity, WrappedResponse<CreateTokenResetPasswordEntity>>> {
        return flow {

            val response = verificationApi.createTokenResetPassword(phone, code)
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                emit(BaseResult.Success(response.body()!!.data!!))

            } else {
                emit(BaseResult.Errors(response.body()!!))
            }
        }
    }
}