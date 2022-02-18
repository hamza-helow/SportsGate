package com.souqApp.presentation.main.more.terms_and_conditions

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
class TermsAndConditionsViewModel @Inject constructor(private val settingsUseCase: SettingsUseCase) :
    ViewModel() {

    private val _state = MutableLiveData<TermsAndConditionsActivityState>()
    val state: LiveData<TermsAndConditionsActivityState> get() = _state

    private fun setLoading(isLoading: Boolean) {
        _state.value = TermsAndConditionsActivityState.Loading(isLoading)
    }

    private fun onError(throwable: Throwable) {
        _state.value = TermsAndConditionsActivityState.Error(throwable)
    }

    private fun onLoaded(contentEntity: ContentEntity) {
        _state.value = TermsAndConditionsActivityState.Loaded(contentEntity)
    }

    private fun onErrorLoad(response: WrappedResponse<ContentEntity>) {
        _state.value = TermsAndConditionsActivityState.ErrorLoad(response)
    }

    @Inject
    fun aboutUs() {
        viewModelScope.launch {
            settingsUseCase.termsAndConditions()
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

sealed class TermsAndConditionsActivityState() {

    data class Loading(val isLoading: Boolean) : TermsAndConditionsActivityState()
    data class Error(val throwable: Throwable) : TermsAndConditionsActivityState()
    data class Loaded(val contentEntity: ContentEntity) : TermsAndConditionsActivityState()
    data class ErrorLoad(val response: WrappedResponse<ContentEntity>) :
        TermsAndConditionsActivityState()
}