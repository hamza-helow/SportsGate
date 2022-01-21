package com.souqApp.domain.forgot_password

import com.souqApp.data.common.remote.dto.TokenResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.forgot_password.remote.dto.ResetPasswordRequest
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import com.souqApp.domain.common.entity.TokenEntity
import kotlinx.coroutines.flow.Flow

interface ForgotPasswordRepository {

    suspend fun requestPasswordReset(phone: String): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>>

    suspend fun createTokenResetPassword(resetPasswordRequest: ResetPasswordRequest): Flow<BaseResult<TokenEntity, WrappedResponse<TokenResponse>>>

    suspend fun resetPassword(newPassword: String): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>>
}