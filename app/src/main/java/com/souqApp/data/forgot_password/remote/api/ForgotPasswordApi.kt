package com.souqApp.data.forgot_password.remote.api

import com.souqApp.data.common.remote.dto.TokenResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.forgot_password.remote.dto.ResetPasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.Query

interface ForgotPasswordApi {

    @POST("v1/users/requestPasswordReset")
    suspend fun requestPasswordReset(@Query("phone") phone: String): Response<WrappedResponse<Nothing>>

    @POST("v1/users/createTokenResetPassword")
    suspend fun createTokenResetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Response<WrappedResponse<TokenResponse>>


}