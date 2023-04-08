package com.souqApp.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

open class BaseViewModel : ViewModel() {

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    suspend fun <T> handelRequest(flow: Flow<T>, onCollect: (T) -> Unit) {
        flow.onStart {
            loadingLiveData.value = true
        }.catch {
            loadingLiveData.value = false
        }.collect {
            onCollect(it)
        }
    }
}