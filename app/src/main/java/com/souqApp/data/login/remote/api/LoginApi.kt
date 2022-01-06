package com.souqApp.data.login.remote.api

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.login.remote.dto.LoginRequest
import com.souqApp.data.common.remote.dto.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("v1/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<WrappedResponse<UserResponse>>
}