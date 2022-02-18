package com.souqApp.presentation.main.more.about_us

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.ContentEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.settings.SettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutUsViewModel  @Inject constructor(private val settingsUseCase: SettingsUseCase) : ViewModel() {

    private val _state = MutableLiveData<AboutUsActivityState>()
    val state: LiveData<AboutUsActivityState> get() = _state

    private fun setLoading(isLoading: Boolean) {
        _state.value = AboutUsActivityState.Loading(isLoading)
    }

    private fun onError(throwable: Throwable) {
        _state.value = AboutUsActivityState.Error(throwable)
    }

    private fun onLoaded(contentEntity: ContentEntity) {
        _state.value = AboutUsActivityState.Loaded(contentEntity)
    }

    private fun onErrorLoad(response: WrappedResponse<ContentEntity>) {
        _state.value = AboutUsActivityState.ErrorLoad(response)
    }

    @Inject
    fun aboutUs() {
        viewModelScope.launch {
            settingsUseCase.aboutUs()
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

sealed class AboutUsActivityState() {

    data class Loading(val isLoading: Boolean) : AboutUsActivityState()
    data class Error(val throwable: Throwable) : AboutUsActivityState()
    data class Loaded(val contentEntity: ContentEntity) : AboutUsActivityState()
    data class ErrorLoad(val response: WrappedResponse<ContentEntity>) : AboutUsActivityState()
}