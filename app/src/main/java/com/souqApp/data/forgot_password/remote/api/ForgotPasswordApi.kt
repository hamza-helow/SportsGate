package com.souqApp.data.forgot_password.remote.api

import com.souqApp.data.common.utlis.WrappedResponse
import retrofit2.Response
import retrofit2.http.Field

interface ForgotPasswordApi {

    suspend fun requestPasswordReset(@Field("phone") phone: String): Response<WrappedResponse<Nothing>>
}