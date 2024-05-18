package com.souqApp.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.settings.remote.dto.PageEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.settings.GetPagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val getPagesUseCase: GetPagesUseCase) :
    ViewModel() {

    private val _state = MutableLiveData<SplashFragmentState>()
    val state: LiveData<SplashFragmentState> get() = _state


    private fun onError(throwable: Throwable) {
        _state.value = SplashFragmentState.Error(throwable)
    }

    private fun onLoaded(pages: List<PageEntity>) {
        _state.value = SplashFragmentState.Loaded(pages)
    }

    private fun onErrorLoad(response: WrappedListResponse<PageEntity>) {
        _state.value = SplashFragmentState.ErrorLoad(response)
    }

    @Inject
    fun getPages() {
        viewModelScope.launch {
            getPagesUseCase.invoke()
                .catch { onError(it) }
                .collect {
                    when (it) {
                        is BaseResult.Success -> onLoaded(it.data)
                        is BaseResult.Errors -> onErrorLoad(it.error)
                    }
                }
        }
    }


}

sealed class SplashFragmentState {

    data class Error(val throwable: Throwable) : SplashFragmentState()

    data class Loaded(val pages: List<PageEntity>) : SplashFragmentState()

    data class ErrorLoad(val response: WrappedListResponse<PageEntity>) :
        SplashFragmentState()
}