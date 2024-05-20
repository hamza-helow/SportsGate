package com.souqApp.data.verifcation

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.common.utlis.handleApi
import com.souqApp.data.verifcation.remote.VerificationApi
import com.souqApp.data.verifcation.remote.dto.ActiveAccountRequest
import com.souqApp.data.verifcation.remote.dto.CreateTokenResetPasswordEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.verifcation.VerificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VerificationRepositoryImpl @Inject constructor(private val verificationApi: VerificationApi) :
    VerificationRepository {
    override suspend fun activeAccount(activeAccountRequest: ActiveAccountRequest): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>> {
        return flow {
            val response = handleApi { verificationApi.activeAccount(activeAccountRequest) }

            if (response.status) {
                val body = response.data
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
                emit(BaseResult.Errors(response))
            }

        }
    }

    override suspend fun createTokenResetPassword(
        phone: String,
        code: String
    ): Flow<BaseResult<CreateTokenResetPasswordEntity, WrappedResponse<CreateTokenResetPasswordEntity>>> {
        return flow {
            val response = handleApi { verificationApi.createTokenResetPassword(phone, code) }
            if (response.status) {
                emit(BaseResult.Success(response.data))

            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

    override suspend fun requestPasswordResetByPhone(phone: String): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {
        return flow {
            val response = handleApi { verificationApi.requestPasswordResetByPhone(phone) }
            if (response.status) {
                emit(BaseResult.Success(EmptyEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

    override suspend fun requestPasswordResetByEmail(email: String): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {
        return flow {
            val response = handleApi { verificationApi.requestPasswordResetByEmail(email) }
            if (response.status) {
                emit(BaseResult.Success(EmptyEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

    override suspend fun resendActivationCode(): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {
        return flow {
            val response = handleApi { verificationApi.resendActivationCode() }
            if (response.status) {
                emit(BaseResult.Success(EmptyEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }

        }
    }
}