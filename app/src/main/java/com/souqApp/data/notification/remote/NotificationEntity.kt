package com.souqApp.data.notification.remote

import com.google.gson.annotations.SerializedName


data class NotificationEntity(
    @SerializedName("body")
    val body: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("notify_type")
    val notifyType: Int,
    @SerializedName("redirect_id")
    val redirectId: Int
)