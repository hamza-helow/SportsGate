package com.souqApp.presentation.forgot_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.forgot_password.ForgotPasswordUseCase
import com.souqApp.presentation.main.category.CategoriesFragmentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val forgotPasswordUseCase: ForgotPasswordUseCase) :
    ViewModel() {

    val state = MutableLiveData<ForgotPasswordActivityState>(ForgotPasswordActivityState.Init)
    val mState: LiveData<ForgotPasswordActivityState> get() = state

    private fun setLoading(isLoading: Boolean) {
        state.value = ForgotPasswordActivityState.Loading(isLoading)
    }

    private fun whenCodeSent() {
        state.value = ForgotPasswordActivityState.CodeSent
    }

    private fun errorSendCode(response: WrappedResponse<Nothing>) {
        state.value = ForgotPasswordActivityState.ErrorSendCode(response)
    }

    private fun whenThrowError(throwable: Throwable) {
        state.value = ForgotPasswordActivityState.Error(throwable)
    }

    private fun requestPasswordReset(phoneNumber: String) {

        viewModelScope.launch {
            forgotPasswordUseCase
                .requestPasswordReset(phoneNumber)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    whenThrowError(it)
                }
                .collect {
                    setLoading(false)
                    when (it) {
                        is BaseResult.Success -> whenCodeSent()
                        is BaseResult.Errors -> errorSendCode(it.error)
                    }
                }
        }
    }

}

sealed class ForgotPasswordActivityState {

    object Init : ForgotPasswordActivityState()
    data class Loading(val isLoading: Boolean) : ForgotPasswordActivityState()
    object CodeSent : ForgotPasswordActivityState()
    data class ErrorSendCode(val response: WrappedResponse<Nothing>) : ForgotPasswordActivityState()
    data class Error(val throwable: Throwable) : ForgotPasswordActivityState()

}