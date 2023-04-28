package com.souqApp.domain.forgot_password

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import kotlinx.coroutines.flow.Flow

interface ForgotPasswordRepository {

    suspend fun requestPasswordReset(phone: String): Flow<BaseResult<EmptyEntity, WrappedResponse<Nothing>>>
}