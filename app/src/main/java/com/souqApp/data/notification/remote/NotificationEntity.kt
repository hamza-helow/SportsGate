package com.souqApp.data.notification.remote

data class NotificationEntities(
    val notifications: List<NotificationEntity>,
    val send_notifications: Boolean
)

data class NotificationEntity(
    val body: String,
    val created_at: String,
    val id: Int,
    val notify_type: Int,
    val redirect_id: Int
)