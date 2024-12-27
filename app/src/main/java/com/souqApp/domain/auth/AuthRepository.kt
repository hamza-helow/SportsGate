package com.souqApp.domain.auth

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.auth.dto.LoginRequest
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.auth.dto.RegisterRequest

interface AuthRepository {

    suspend fun changePassword(oldPassword: String, newPassword: String): WrappedResponse<Nothing>

    suspend fun resetPassword(newPassword: String, resetToken: String): WrappedResponse<Nothing>

    suspend fun requestPasswordReset(phone: String): WrappedResponse<Nothing>

    suspend fun login(loginRequest: LoginRequest): WrappedResponse<UserResponse>

    suspend fun register(registerRequest: RegisterRequest): WrappedResponse<UserResponse>

    suspend fun updateUser(name: String, image: String): WrappedResponse<UserResponse>

    suspend fun deleteUser(email: String): WrappedListResponse<Any>

    suspend fun sendOtpByPhone(): WrappedResponse<Nothing>

    suspend fun sendOtpByEmail(): WrappedResponse<Nothing>

    suspend fun verifyMobileNumber(code: String): WrappedResponse<UserResponse>

    suspend fun verifyEmail(code: String): WrappedResponse<UserResponse>
}