package com.souqApp.domain.auth.usecase

import com.souqApp.domain.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreatePasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun invoke(newPassword: String, resetToken: String): Flow<Boolean> {
        return flow {
            val response = authRepository.resetPassword(newPassword, resetToken)
            emit(response.status)
        }
    }
}
