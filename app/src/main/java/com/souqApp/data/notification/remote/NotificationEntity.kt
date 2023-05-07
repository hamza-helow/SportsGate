package com.souqApp.data.notification.remote

import com.google.gson.annotations.SerializedName

data class NotificationEntities(
    @SerializedName("notifications")
    val notifications: List<NotificationEntity>,
)

data class NotificationEntity(
    @SerializedName("body")
    val body: String,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("notify_type")
    val notify_type: Int,
    @SerializedName("redirect_id")
    val redirect_id: Int
)