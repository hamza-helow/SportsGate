package com.souqApp.data.notification.remote

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.orders.remote.OrderResponse
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale

interface NotificationApi {

    @GET("v2/users/notifications")
    suspend fun notificationsHistory(
        @Query("lang") language: String = Locale.getDefault().language,
        @Query("unread") unread: Boolean = true,
        @Query("page") page: Int = 1
    ): WrappedListResponse<NotificationEntity>
}