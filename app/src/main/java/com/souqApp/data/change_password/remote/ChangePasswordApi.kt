package com.souqApp.data.change_password.remote

import com.souqApp.data.common.utlis.WrappedResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface ChangePasswordApi {

    @POST("v1/users/changePassword")
    suspend fun changePassword(
        @Query("old_password") oldPassword: String,
        @Query("new_password") newPassword: String
    ): Response<WrappedResponse<Nothing>>

}