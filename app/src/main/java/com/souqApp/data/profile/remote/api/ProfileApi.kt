package com.souqApp.data.profile.remote.api

import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {

    @Multipart
    @POST("v1/users/updateProfile")
    suspend fun updateUser(
        @Part filePart: MultipartBody.Part,
        @Part namePart: MultipartBody.Part
    ): Response<WrappedResponse<UserResponse>>


}