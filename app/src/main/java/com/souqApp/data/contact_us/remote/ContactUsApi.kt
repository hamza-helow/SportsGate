package com.souqApp.data.contact_us.remote

import com.souqApp.data.common.utlis.WrappedResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ContactUsApi {
    @POST("v1/settings/sendContactUs")
    suspend fun sendContactUs(@Body request: ContactUsRequest): WrappedResponse<Nothing>
}
