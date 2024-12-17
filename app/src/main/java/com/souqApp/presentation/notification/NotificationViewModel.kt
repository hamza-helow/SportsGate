package com.souqApp.presentation.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.souqApp.data.notification.remote.NotificationEntity
import com.souqApp.domain.notification.NotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(notificationUseCase: NotificationUseCase) :
    ViewModel() {
    val notifications: LiveData<PagingData<NotificationEntity>> =
        notificationUseCase.invoke().cachedIn(viewModelScope)
}
