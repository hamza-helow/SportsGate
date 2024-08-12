package com.souqApp.data.verifcation.remote

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.dto.ActiveAccountRequest
import com.souqApp.data.verifcation.remote.dto.CreateTokenResetPasswordEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface VerificationApi {

    @POST("v2/users/activeAccount")
    suspend fun activeAccount(@Body activeAccountRequest: ActiveAccountRequest): WrappedResponse<UserResponse>


    @POST("v2/users/createTokenResetPassword")
    suspend fun createTokenResetPasswordByPhone(
        @Query("phone") phone: String,
        @Query("reset_code") code: String
    ): WrappedResponse<CreateTokenResetPasswordEntity>

    @POST("v2/users/createTokenResetPassword")
    suspend fun createTokenResetPasswordByEmail(
        @Query("email") phone: String,
        @Query("reset_code") code: String
    ): WrappedResponse<CreateTokenResetPasswordEntity>


    @POST("v2/users/requestPasswordResetByPhone")
    suspend fun requestPasswordResetByPhone(@Query("phone") phone: String): WrappedResponse<Nothing>

    @POST("v2/users/requestPasswordResetByEmail")
    suspend fun requestPasswordResetByEmail(@Query("email") phone: String): WrappedResponse<Nothing>


    @POST("v2/users/resendActivationCode")
    suspend fun resendActivationCode(): WrappedResponse<Nothing>


    @GET("v2/users/requestPasswordSupportedMethods")
    suspend fun getPasswordSupportedMethods(): WrappedListResponse<String>

}