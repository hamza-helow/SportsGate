package com.souqApp.data.create_password.remote

import com.souqApp.data.common.utlis.WrappedResponse
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface CreatePasswordApi {

    @POST("v1/users/resetPassword")
    suspend fun resetPassword(
        @Query("new_password") newPassword: String,
        @Header("Authorization") resetToken: String
    ): WrappedResponse<Nothing>
}