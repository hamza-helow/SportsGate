package com.souqApp.presentation.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.notification.remote.NotificationEntities
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.notification.NotificationUseCase
import com.souqApp.infra.utils.SharedPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationUseCase: NotificationUseCase,
    sharedPrefs: SharedPrefs
) :
    ViewModel() {


    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    val notificationsLiveData: MutableLiveData<BaseResult<NotificationEntities, WrappedResponse<NotificationEntities>>> =
        MutableLiveData()

    private fun setLoading(isLoading: Boolean) {
        loadingLiveData.value = isLoading
    }


    init {
        if (sharedPrefs.isLogin())
            getNotificationsHistory()
    }


    private fun getNotificationsHistory() {
        viewModelScope.launch {
            notificationUseCase.notificationsHistory()
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    notificationsLiveData.value = it
                }
        }
    }
}
