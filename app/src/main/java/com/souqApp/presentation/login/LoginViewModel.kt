package com.souqApp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.login.remote.dto.LoginRequest
import com.souqApp.data.verifcation.remote.dto.ActiveAccountRequest
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.login.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val state = MutableStateFlow<LoginActivityState>(LoginActivityState.Init)
    private var loginByPhone: Boolean = true

    val mState: StateFlow<LoginActivityState> get() = state
    val isPhoneEnable: Boolean get() = loginByPhone


    private fun setLoading() {
        state.value = LoginActivityState.IsLoading(true)
    }

    fun loginByPhoneToggle() {
        loginByPhone = !loginByPhone
        state.value = LoginActivityState.LoginByPhone(loginByPhone)
    }

    private fun hideLoading() {
        state.value = LoginActivityState.IsLoading(false)
    }

    private fun showToast(message: String) {
        state.value = LoginActivityState.ShowToast(message)
    }


    private fun successLogin(loginEntity: UserEntity) {
        state.value = LoginActivityState.SuccessLogin(loginEntity)
    }

    private fun errorLogin(rawResponse: WrappedResponse<UserResponse>) {
        state.value = LoginActivityState.ErrorLogin(rawResponse)
    }


    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            loginUseCase.invoke(loginRequest).onStart {
                setLoading()
            }.catch { error ->
                hideLoading()
                showToast(error.stackTraceToString())
            }.collect { result ->
                hideLoading()
                when (result) {
                    is BaseResult.Success -> successLogin(result.data)
                    is BaseResult.Errors -> errorLogin(result.error)
                }
            }

        }
    }

}

sealed class LoginActivityState {
    object Init : LoginActivityState()
    data class IsLoading(val isLoading: Boolean) : LoginActivityState()
    data class LoginByPhone(val isEnable: Boolean) : LoginActivityState()
    data class ShowToast(val message: String) : LoginActivityState()
    data class SuccessLogin(val loginEntity: UserEntity) : LoginActivityState()
    data class ErrorLogin(val rawResponse: WrappedResponse<UserResponse>) : LoginActivityState()
}