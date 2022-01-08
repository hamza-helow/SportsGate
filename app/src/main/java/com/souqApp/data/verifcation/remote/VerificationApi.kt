package com.souqApp.data.verifcation.remote

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.dto.ActiveAccountRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface VerificationApi {

    @POST("v1/users/activeAccount")
    suspend fun activeAccount(@Body activeAccountRequest: ActiveAccountRequest): Response<WrappedResponse<UserResponse>>
}