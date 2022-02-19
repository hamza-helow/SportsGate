package com.souqApp.domain.create_password

import kotlinx.coroutines.flow.Flow

interface CreatePasswordRepository {

    suspend fun resetPassword(newPassword: String, resetToken: String): Flow<Boolean>
}