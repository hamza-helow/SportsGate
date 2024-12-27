package com.souqApp.domain.verifcation.usecase

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.dto.CreateTokenResetPasswordEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import com.souqApp.domain.verifcation.VerificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateResetTokenUseCase @Inject constructor(
    private val verificationRepository: VerificationRepository
) {

    suspend fun invoke(
        byPhone: Boolean,
        credentialId: String,
        code: String
    ): Flow<BaseResult<CreateTokenResetPasswordEntity, WrappedResponse<CreateTokenResetPasswordEntity>>> {
        return flow {
            val response =
                if (byPhone)
                    verificationRepository.createTokenResetPasswordByPhone(credentialId, code)
                else
                    verificationRepository.createTokenResetPasswordByEmail(credentialId, code)

            if (response.status) {
                emit(BaseResult.Success(response.data))

            } else {
                emit(BaseResult.Errors(response))
            }


        }
    }
}