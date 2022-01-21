package com.souqApp.data.change_password.remote

import com.souqApp.data.change_password.remote.dto.ChangePasswordRequest
import com.souqApp.data.common.utlis.WrappedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChangePasswordApi {

    @POST("v1/users/changePassword")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<WrappedResponse<Nothing>>

}