package com.souqApp.domain.notification

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.notification.remote.NotificationEntities
import com.souqApp.data.notification.remote.NotificationEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    suspend fun notificationsHistory(): Flow<BaseResult<NotificationEntities, WrappedResponse<NotificationEntities>>>
}