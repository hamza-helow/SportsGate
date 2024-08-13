package com.souqApp.data.register.remote.api

import com.souqApp.data.common.remote.dto.TokenResponse
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.register.remote.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface RegisterApi {

    @POST("v2/users/register")
    suspend fun register(@Body registerRequest: RegisterRequest): WrappedResponse<TokenResponse>




}