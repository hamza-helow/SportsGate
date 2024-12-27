package com.souqApp.domain.verifcation.usecase

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.dto.CreateTokenResetPasswordEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import com.souqApp.domain.verifcation.VerificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RequestPasswordResetUseCase @Inject constructor(
    private val verificationRepository: VerificationRepository
) {

    suspend fun invoke(
        credentialId: String,
        isPhone: Boolean
    ): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {
        return flow {
            val response =
                if (isPhone)
                    verificationRepository.requestPasswordResetByPhone(credentialId)
                else
                    verificationRepository.requestPasswordResetByEmail(credentialId)

            if (response.status) {
                emit(BaseResult.Success(EmptyEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}