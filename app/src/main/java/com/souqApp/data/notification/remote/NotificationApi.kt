package com.souqApp.data.notification.remote

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import retrofit2.Response
import retrofit2.http.GET

interface NotificationApi {

    @GET("v1/users/notificationsHistory")
    suspend fun notificationsHistory(): Response<WrappedResponse<NotificationEntities>>
}