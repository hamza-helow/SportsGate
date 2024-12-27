package com.souqApp.domain.auth.usecase

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.auth.AuthRepository
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun invoke(
        oldPassword: String,
        newPassword: String
    ): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {

        return flow {
            val response = authRepository.changePassword(oldPassword, newPassword)
            val isSuccessful = response.status
            if (isSuccessful) {
                emit(BaseResult.Success(EmptyEntity()))
            } else {
                emit(BaseResult.Errors(response))

            }
        }
    }
}
