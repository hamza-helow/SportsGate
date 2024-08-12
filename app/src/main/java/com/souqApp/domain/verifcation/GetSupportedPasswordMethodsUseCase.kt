package com.souqApp.domain.verifcation

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetSupportedPasswordMethodsUseCase @Inject constructor(private val verificationRepository: VerificationRepository) {

    suspend fun invoke(): Flow<BaseResult<List<String>, WrappedListResponse<String>>> {
        return verificationRepository.getPasswordSupportedMethods()
    }

}