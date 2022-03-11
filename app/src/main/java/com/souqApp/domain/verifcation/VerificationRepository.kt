package com.souqApp.domain.verifcation

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.dto.ActiveAccountRequest
import com.souqApp.data.verifcation.remote.dto.CreateTokenResetPasswordEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import com.souqApp.domain.common.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface VerificationRepository {

    suspend fun activeAccount(activeAccountRequest: ActiveAccountRequest): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>>

    suspend fun createTokenResetPassword(
        phone: String,
        code: String
    ): Flow<BaseResult<CreateTokenResetPasswordEntity, WrappedResponse<CreateTokenResetPasswordEntity>>>

    suspend fun requestPasswordReset(phone: String): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>>

    suspend fun resendActivationCode(): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>>
}