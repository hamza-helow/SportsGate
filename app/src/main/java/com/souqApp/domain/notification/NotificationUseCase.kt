package com.souqApp.domain.notification

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.notification.remote.NotificationEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationUseCase @Inject constructor(private val notificationRepository: NotificationRepository) {

    fun invoke(): LiveData<PagingData<NotificationEntity>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { NotificationsPagingSource(notificationRepository) }
    ).liveData
}