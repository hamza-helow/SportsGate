package com.souqApp.data.notification.remote

import com.souqApp.data.common.utlis.WrappedResponse
import retrofit2.http.GET

interface NotificationApi {

    @GET("v2/users/notificationsHistory")
    suspend fun notificationsHistory(): WrappedResponse<NotificationEntities>
}