package com.souqApp.presentation.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.notification.remote.NotificationEntities
import com.souqApp.data.notification.remote.NotificationEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.notification.NotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val notificationUseCase: NotificationUseCase) :
    ViewModel() {

    private val _state = MutableLiveData<NotificationActivityState>()
    val state: LiveData<NotificationActivityState> get() = _state

    private fun setLoading(isLoading: Boolean) {
        _state.value = NotificationActivityState.Loading(isLoading)
    }

    private fun onError(throwable: Throwable) {
        _state.value = NotificationActivityState.Error(throwable)
    }

    private fun onLoaded(notifications: NotificationEntities) {
        _state.value = NotificationActivityState.Loaded(notifications)
    }

    private fun onErrorLoad(response: WrappedResponse<NotificationEntities>) {
        _state.value = NotificationActivityState.ErrorLoad(response)
    }

    @Inject
    fun getNotificationsHistory() {
        viewModelScope.launch {
            notificationUseCase.notificationsHistory()
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    when (it) {
                        is BaseResult.Success -> onLoaded(it.data)
                        is BaseResult.Errors -> onErrorLoad(it.error)
                    }
                }
        }
    }
}


sealed class NotificationActivityState {

    data class Loading(val isLoading: Boolean) : NotificationActivityState()
    data class Error(val throwable: Throwable) : NotificationActivityState()
    data class Loaded(val notifications: NotificationEntities) : NotificationActivityState()
    data class ErrorLoad(val response: WrappedResponse<NotificationEntities>) :
        NotificationActivityState()
}