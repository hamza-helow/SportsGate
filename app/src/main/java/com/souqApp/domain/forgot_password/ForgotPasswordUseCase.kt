package com.souqApp.domain.forgot_password

import com.souqApp.data.common.remote.dto.TokenResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.forgot_password.remote.dto.ResetPasswordRequest
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import com.souqApp.domain.common.entity.TokenEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(private val forgotPasswordRepository: ForgotPasswordRepository) {

    suspend fun requestPasswordReset(phone: String): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {
        return forgotPasswordRepository.requestPasswordReset(phone)
    }

    suspend fun createTokenResetPassword(resetPasswordRequest: ResetPasswordRequest): Flow<BaseResult<TokenEntity, WrappedResponse<TokenResponse>>> {
        return forgotPasswordRepository.createTokenResetPassword(resetPasswordRequest)
    }

    suspend fun resetPassword(newPassword: String): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {
        return forgotPasswordRepository.resetPassword(newPassword)
    }
}