package com.souqApp.data.verifcation.remote

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.dto.ActiveAccountRequest
import com.souqApp.data.verifcation.remote.dto.CreateTokenResetPasswordEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface VerificationApi {

    @POST("v1/users/activeAccount")
    suspend fun activeAccount(@Body activeAccountRequest: ActiveAccountRequest): Response<WrappedResponse<UserResponse>>

    @POST("v1/users/createTokenResetPassword")
    suspend fun createTokenResetPassword(
        @Query("phone") phone: String,
        @Query("reset_code") code: String
    ): Response<WrappedResponse<CreateTokenResetPasswordEntity>>
}