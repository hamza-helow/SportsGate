package com.souqApp.data.forgot_password.repoistory

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.forgot_password.remote.api.ForgotPasswordApi
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import com.souqApp.domain.forgot_password.ForgotPasswordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ForgotPasswordRepositoryImpl @Inject constructor(private val forgotPasswordApi: ForgotPasswordApi) :
    ForgotPasswordRepository {
    override suspend fun requestPasswordReset(phone: String): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> {
        return flow {
            val response = forgotPasswordApi.requestPasswordReset(phone)
            if (response.status) {
                emit(BaseResult.Success(EmptyEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }

        }
    }
}