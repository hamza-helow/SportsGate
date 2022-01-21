package com.souqApp.data.register.remote.api

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.register.remote.dto.RegisterRequest
import com.souqApp.data.common.remote.dto.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {

    @POST("v1/users/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<WrappedResponse<TokenResponse>>

}