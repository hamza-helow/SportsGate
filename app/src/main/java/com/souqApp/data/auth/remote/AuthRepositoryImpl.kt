package com.souqApp.data.auth.remote

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.auth.dto.LoginRequest
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.auth.dto.RegisterRequest
import com.souqApp.domain.auth.AuthRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authApi: AuthApi) :
    AuthRepository {

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): WrappedResponse<Nothing> {
        return authApi.changePassword(oldPassword, newPassword)
    }

    override suspend fun resetPassword(
        newPassword: String,
        resetToken: String
    ): WrappedResponse<Nothing> {
        return authApi.resetPassword(newPassword, resetToken)
    }


    override suspend fun requestPasswordReset(phone: String): WrappedResponse<Nothing> {
        return authApi.requestPasswordReset(phone)
    }

    override suspend fun login(loginRequest: LoginRequest): WrappedResponse<UserResponse> {
        return authApi.login(loginRequest)
    }

    override suspend fun register(registerRequest: RegisterRequest): WrappedResponse<UserResponse> {
        return authApi.register(registerRequest)
    }


    override suspend fun updateUser(
        name: String,
        image: String
    ): WrappedResponse<UserResponse> {
        val imageRequestBody =
            File(image).asRequestBody("application/octet-stream".toMediaTypeOrNull())

        val body: RequestBody =
            MultipartBody
                .Builder()
                .setType(MultipartBody.FORM).apply {
                    if (image.isNotEmpty()) {
                        addFormDataPart("image", "file", imageRequestBody)
                    }
                    addFormDataPart("name", name)

                }.build()

        return authApi.updateUser(body)

    }

    override suspend fun deleteUser(email: String): WrappedListResponse<Any> {
        return authApi.deleteUser(email)
    }

    override suspend fun sendOtpByPhone(): WrappedResponse<Nothing> {
        return authApi.sendOtpByPhone()
    }

    override suspend fun sendOtpByEmail(): WrappedResponse<Nothing> {
        return authApi.sendOtpByEmail()
    }

    override suspend fun verifyMobileNumber(code: String): WrappedResponse<UserResponse> {
        return authApi.verifyMobile(code)
    }

    override suspend fun verifyEmail(code: String): WrappedResponse<UserResponse> {
        return authApi.verifyEmail(code)
    }

}