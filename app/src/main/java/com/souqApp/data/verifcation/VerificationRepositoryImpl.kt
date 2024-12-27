package com.souqApp.data.verifcation

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.VerificationApi
import com.souqApp.data.verifcation.remote.dto.CreateTokenResetPasswordEntity
import com.souqApp.domain.verifcation.VerificationRepository
import javax.inject.Inject

class VerificationRepositoryImpl @Inject constructor(private val verificationApi: VerificationApi) :
    VerificationRepository {

    override suspend fun createTokenResetPasswordByPhone(
        phone: String,
        code: String
    ): WrappedResponse<CreateTokenResetPasswordEntity> {
        return verificationApi.createTokenResetPasswordByPhone(phone, code)
    }

    override suspend fun createTokenResetPasswordByEmail(
        phone: String,
        code: String
    ): WrappedResponse<CreateTokenResetPasswordEntity> {
        return verificationApi.createTokenResetPasswordByEmail(phone, code)
    }

    override suspend fun requestPasswordResetByPhone(phone: String): WrappedResponse<Nothing> {
        return verificationApi.requestPasswordResetByPhone(phone)
    }

    override suspend fun requestPasswordResetByEmail(email: String): WrappedResponse<Nothing> {
        return verificationApi.requestPasswordResetByEmail(email)
    }


    override suspend fun getPasswordSupportedMethods(): WrappedListResponse<String> {
        return verificationApi.getPasswordSupportedMethods()
    }
}