package com.souqApp.data.profile.remote.api

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.login.remote.dto.LoginRequest
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ProfileApi {

    @POST("v1/users/updateProfile")
    suspend fun updateUser(
        @Body params: RequestBody
    ): WrappedResponse<UserResponse>


    @POST("v1/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): WrappedResponse<UserResponse>

}