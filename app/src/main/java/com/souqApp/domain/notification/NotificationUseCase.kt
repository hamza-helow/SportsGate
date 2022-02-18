package com.souqApp.domain.notification

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.notification.remote.NotificationEntities
import com.souqApp.data.notification.remote.NotificationEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationUseCase @Inject constructor(private val notificationRepository: NotificationRepository) {

    suspend fun notificationsHistory(): Flow<BaseResult<NotificationEntities, WrappedResponse<NotificationEntities>>> {
        return notificationRepository.notificationsHistory()
    }
}