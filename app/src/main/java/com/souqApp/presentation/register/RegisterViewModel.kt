package com.souqApp.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.register.remote.dto.RegisterRequest
import com.souqApp.data.common.remote.dto.TokenResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.TokenEntity
import com.souqApp.domain.register.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) :
    ViewModel() {

    private val state = MutableStateFlow<RegisterActivityState>(RegisterActivityState.Init)
    val mState: StateFlow<RegisterActivityState> get() = state

    private fun setLoading() {
        state.value = RegisterActivityState.IsLoading(true)
    }

    private fun hideLoading() {
        state.value = RegisterActivityState.IsLoading(false)
    }

    private fun showToast(message: String) {
        state.value = RegisterActivityState.ShowToast(message)
    }

    private fun successRegister(tokenEntity: TokenEntity) {
        state.value = RegisterActivityState.SuccessRegister(tokenEntity)
    }

    private fun errorRegister(rawResponse: WrappedResponse<TokenResponse>) {
        state.value = RegisterActivityState.ErrorRegister(rawResponse)
    }

    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            registerUseCase.invokeRegister(registerRequest).onStart {
                setLoading()
            }.catch {
                hideLoading()
                showToast(it.stackTraceToString())
            }.collect {
                hideLoading()
                when (it) {
                    is BaseResult.Success -> successRegister(it.data)
                    is BaseResult.Errors -> errorRegister(it.error)
                }
            }
        }
    }

}

sealed class RegisterActivityState {
    object Init : RegisterActivityState()
    data class IsLoading(val isLoading: Boolean) : RegisterActivityState()
    data class ShowToast(val message: String) : RegisterActivityState()
    data class SuccessRegister(val tokenEntity: TokenEntity) : RegisterActivityState()
    data class ErrorRegister(val rawResponse: WrappedResponse<TokenResponse>) :
        RegisterActivityState()
}