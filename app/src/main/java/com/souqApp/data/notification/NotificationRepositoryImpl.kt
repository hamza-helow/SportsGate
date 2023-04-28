package com.souqApp.data.notification

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.notification.remote.NotificationApi
import com.souqApp.data.notification.remote.NotificationEntities
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.notification.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(private val notificationApi: NotificationApi) :
    NotificationRepository {
    override suspend fun notificationsHistory(): Flow<BaseResult<NotificationEntities, WrappedResponse<NotificationEntities>>> {
        return flow {
            val response = notificationApi.notificationsHistory()
            if (response.status) {
                emit(BaseResult.Success(response.data))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}