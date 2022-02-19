package com.souqApp.domain.create_password

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreatePasswordUseCase @Inject constructor(private val createPasswordRepository: CreatePasswordRepository) {

    suspend fun resetPassword(newPassword: String, resetToken: String): Flow<Boolean> {
        return createPasswordRepository.resetPassword(newPassword, "Bearer $resetToken")
    }
}
