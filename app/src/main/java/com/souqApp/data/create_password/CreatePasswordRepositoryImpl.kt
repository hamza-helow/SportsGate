package com.souqApp.data.create_password

import com.souqApp.data.create_password.remote.CreatePasswordApi
import com.souqApp.domain.create_password.CreatePasswordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreatePasswordRepositoryImpl @Inject constructor(private val createPasswordApi: CreatePasswordApi) :
    CreatePasswordRepository {
    override suspend fun resetPassword(newPassword: String, resetToken: String): Flow<Boolean> {
        return flow {
            val response = createPasswordApi.resetPassword(newPassword, resetToken)
            val isSuccessful = response.body()?.status
            emit(isSuccessful == true)
        }
    }
}