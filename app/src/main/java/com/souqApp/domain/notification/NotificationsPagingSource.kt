package com.souqApp.domain.notification

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.souqApp.data.notification.remote.NotificationEntity
import com.souqApp.domain.common.BaseResult

class NotificationsPagingSource(private val notificationRepository: NotificationRepository) :
    PagingSource<Int, NotificationEntity>() {


    override fun getRefreshKey(state: PagingState<Int, NotificationEntity>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationEntity> {

        val pageNumber = params.key ?: 1
        val response = notificationRepository.notificationsHistory(pageNumber)
        var notifications: List<NotificationEntity> = listOf()

        response.collect {
            when (it) {
                is BaseResult.Errors -> Unit
                is BaseResult.Success -> {
                    notifications = it.data
                }
            }
        }

        return LoadResult.Page(
            data = notifications,
            prevKey = if (pageNumber == 1) null else (pageNumber - 1),
            nextKey = if (notifications.isEmpty()) null else (pageNumber + 1)
        )
    }
}