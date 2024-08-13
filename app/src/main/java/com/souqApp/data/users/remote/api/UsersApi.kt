package com.souqApp.data.users.remote.api

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.login.remote.dto.LoginRequest
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface UsersApi {

    @POST("v2/users/updateProfile")
    suspend fun updateUser(
        @Body params: RequestBody
    ): WrappedResponse<UserResponse>

    @POST("v2/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): WrappedResponse<UserResponse>


    @POST("v2/users/dropAccount")
    suspend fun deleteUser(@Query("email") email: String): WrappedListResponse<Any>

    @POST("v2/users/resendPhoneOTP")
    suspend fun sendOtpByPhone(): WrappedResponse<Nothing>

    @POST("v2/users/resendEmailOTP")
    suspend fun sendOtpByEmail(): WrappedResponse<Nothing>

    @POST("v2/users/verifyPhoneNumberByOTP")
    suspend fun verifyMobile(
        @Query("code") code: String,
        @Query("device_type") deviceType: Int = 0
    ): WrappedResponse<UserResponse>

    @POST("v2/users/verifyEmailByOTP")
    suspend fun verifyEmail(
        @Query("code") code: String,
        @Query("device_type") deviceType: Int = 0
    ): WrappedResponse<UserResponse>

}