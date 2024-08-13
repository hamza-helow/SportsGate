package com.souqApp.domain.verifcation

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.dto.ActiveAccountRequest
import com.souqApp.data.verifcation.remote.dto.CreateTokenResetPasswordEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import com.souqApp.domain.common.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerificationUseCase @Inject constructor(
    private val verificationRepository: VerificationRepository
) {

    suspend fun invokeActiveAccount(activeAccountRequest: ActiveAccountRequest): Flow<BaseResult<UserEntity, WrappedResponse<UserResponse>>> {
        return verificationRepository.activeAccount(activeAccountRequest)
    }

    suspend fun createTokenResetPassword(
        byPhone: Boolean,
        credentialId: String,
        code: String
    ): Flow<BaseResult<CreateTokenResetPasswordEntity, WrappedResponse<CreateTokenResetPasswordEntity>>> {
        return if (byPhone)
            verificationRepository.createTokenResetPasswordByPhone(credentialId, code)
        else
            verificationRepository.createTokenResetPasswordByEmail(credentialId, code)
    }

    suspend fun requestPasswordReset(
        credentialId: String,
        isPhone: Boolean
    ): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {
        return if (isPhone)
            verificationRepository.requestPasswordResetByPhone(credentialId)
        else
            verificationRepository.requestPasswordResetByEmail(credentialId)
    }

    suspend fun resendActivationCode(): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {
        return verificationRepository.resendActivationCode()
    }

}