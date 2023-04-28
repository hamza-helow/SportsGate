package com.souqApp.data.forgot_password.remote.api

import com.souqApp.data.common.utlis.WrappedResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface ForgotPasswordApi {

    @POST("v1/users/requestPasswordReset")
    suspend fun requestPasswordReset(@Query("phone") phone: String): WrappedResponse<Nothing>
}