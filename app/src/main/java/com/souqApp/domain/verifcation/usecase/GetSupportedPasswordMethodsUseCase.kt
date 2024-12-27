package com.souqApp.domain.verifcation.usecase

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.verifcation.VerificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GetSupportedPasswordMethodsUseCase @Inject constructor(private val verificationRepository: VerificationRepository) {

    suspend fun invoke(): Flow<BaseResult<List<String>, WrappedListResponse<String>>> {
        return flow {
            val response = verificationRepository.getPasswordSupportedMethods()
            if (response.status) {
                emit(BaseResult.Success(response.data.orEmpty()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

}