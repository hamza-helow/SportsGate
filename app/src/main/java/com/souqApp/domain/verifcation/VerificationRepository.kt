package com.souqApp.domain.verifcation

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.dto.CreateTokenResetPasswordEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import kotlinx.coroutines.flow.Flow

interface VerificationRepository {

    suspend fun createTokenResetPasswordByPhone(
        phone: String,
        code: String
    ): WrappedResponse<CreateTokenResetPasswordEntity>

    suspend fun createTokenResetPasswordByEmail(
        phone: String,
        code: String
    ):  WrappedResponse<CreateTokenResetPasswordEntity>

    suspend fun requestPasswordResetByPhone(phone: String): WrappedResponse<Nothing>

    suspend fun requestPasswordResetByEmail(email: String): WrappedResponse<Nothing>

    suspend fun getPasswordSupportedMethods(): WrappedListResponse<String>
}