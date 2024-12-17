package com.souqApp.data.notification

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.handleApi
import com.souqApp.data.notification.remote.NotificationApi
import com.souqApp.data.notification.remote.NotificationEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.notification.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(private val notificationApi: NotificationApi) :
    NotificationRepository {

    override suspend fun notificationsHistory(pageNumber: Int): Flow<BaseResult<List<NotificationEntity>, WrappedListResponse<NotificationEntity>>> {
        return flow {
            val response = handleApi { notificationApi.notificationsHistory(page = pageNumber) }
            if (response.status) {
                emit(BaseResult.Success(response.data.orEmpty()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}