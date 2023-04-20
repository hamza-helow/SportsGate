package com.souqApp.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.register.remote.dto.RegisterRequest
import com.souqApp.data.common.remote.dto.TokenResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.TokenEntity
import com.souqApp.domain.register.usecase.RegisterUseCase
import com.souqApp.infra.extension.isEmail
import com.souqApp.infra.extension.isPasswordValid
import com.souqApp.infra.extension.isPhone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) :
    ViewModel() {

    private val state = MutableStateFlow<RegisterFragmentState>(RegisterFragmentState.Init)
    val mState: StateFlow<RegisterFragmentState> get() = state


    fun validate(
        fullName: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ) {
        state.value = RegisterFragmentState.Validate(
            fullName.isNotBlank() && email.isEmail() &&
                    phone.isPhone() && password.isPasswordValid() &&
                    password == confirmPassword
        )
    }

    fun resetState() {
        state.value = RegisterFragmentState.Init
    }

    private fun setLoading() {
        state.value = RegisterFragmentState.IsLoading(true)
    }

    private fun hideLoading() {
        state.value = RegisterFragmentState.IsLoading(false)
    }

    private fun showToast(message: String) {
        state.value = RegisterFragmentState.ShowToast(message)
    }

    private fun successRegister(tokenEntity: TokenEntity) {
        state.value = RegisterFragmentState.SuccessRegister(tokenEntity)
    }

    private fun errorRegister(rawResponse: WrappedResponse<TokenResponse>) {
        state.value = RegisterFragmentState.ErrorRegister(rawResponse)
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

sealed class RegisterFragmentState {
    object Init : RegisterFragmentState()
    data class IsLoading(val isLoading: Boolean) : RegisterFragmentState()
    data class ShowToast(val message: String) : RegisterFragmentState()
    data class SuccessRegister(val tokenEntity: TokenEntity) : RegisterFragmentState()
    data class ErrorRegister(val rawResponse: WrappedResponse<TokenResponse>) :
        RegisterFragmentState()

    data class Validate(val isValid: Boolean) : RegisterFragmentState()
}