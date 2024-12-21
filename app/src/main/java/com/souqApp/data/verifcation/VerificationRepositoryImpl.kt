package com.souqApp.data.verifcation

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
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
            val response = verificationApi.activeAccount(activeAccountRequest)

            if (response.status) {
                val body = response.data
                emit(BaseResult.Success(body.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }

        }
    }

    override suspend fun createTokenResetPasswordByPhone(
        phone: String,
        code: String
    ): Flow<BaseResult<CreateTokenResetPasswordEntity, WrappedResponse<CreateTokenResetPasswordEntity>>> {
        return flow {
            val response = verificationApi.createTokenResetPasswordByPhone(phone, code)
            if (response.status) {
                emit(BaseResult.Success(response.data))

            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

    override suspend fun createTokenResetPasswordByEmail(
        phone: String,
        code: String
    ): Flow<BaseResult<CreateTokenResetPasswordEntity, WrappedResponse<CreateTokenResetPasswordEntity>>> {
        return flow {
            val response = verificationApi.createTokenResetPasswordByEmail(phone, code)
            if (response.status) {
                emit(BaseResult.Success(response.data))

            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

    override suspend fun requestPasswordResetByPhone(phone: String): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {
        return flow {
            val response =  verificationApi.requestPasswordResetByPhone(phone)
            if (response.status) {
                emit(BaseResult.Success(EmptyEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

    override suspend fun requestPasswordResetByEmail(email: String): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {
        return flow {
            val response =  verificationApi.requestPasswordResetByEmail(email)
            if (response.status) {
                emit(BaseResult.Success(EmptyEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

    override suspend fun resendActivationCode(): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {
        return flow {
            val response = verificationApi.resendActivationCode()
            if (response.status) {
                emit(BaseResult.Success(EmptyEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }

        }
    }

    override suspend fun getPasswordSupportedMethods(): Flow<BaseResult<List<String>, WrappedListResponse<String>>> {
        return flow {
            val response = verificationApi.getPasswordSupportedMethods()
            if (response.status) {
                emit(BaseResult.Success(response.data.orEmpty()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}