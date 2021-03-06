package com.souqApp.presentation.main.more.changePassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.change_password.remote.dto.ChangePasswordRequest
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.change_password.ChangePasswordUseCase
import com.souqApp.domain.common.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private val changePasswordUseCase: ChangePasswordUseCase) :
    ViewModel() {

    private val state =
        MutableStateFlow<ChangePasswordActivityState>(ChangePasswordActivityState.Init)
    val mState: StateFlow<ChangePasswordActivityState> get() = state

    private fun setLoading(isLoading: Boolean) {
        state.value = ChangePasswordActivityState.Loading(isLoading)
    }

    private fun whenAnErrorOccurs(throwable: Throwable) {
        state.value = ChangePasswordActivityState.Error(throwable)
    }

    private fun errorChangePassword(response: WrappedResponse<Nothing>) {
        state.value = ChangePasswordActivityState.ErrorChangePassword(response)
    }

    private fun successChangePassword() {
        state.value = ChangePasswordActivityState.SuccessChangePassword
    }

    fun changePassword(changePasswordRequest: ChangePasswordRequest) {
        viewModelScope.launch {

            changePasswordUseCase
                .changePassword(changePasswordRequest)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    whenAnErrorOccurs(it)
                }
                .collect {
                    setLoading(false)
                    when (it) {
                        is BaseResult.Success -> successChangePassword()
                        is BaseResult.Errors -> errorChangePassword(it.error)
                    }
                }

        }
    }

}

sealed class ChangePasswordActivityState {
    object Init : ChangePasswordActivityState()
    object SuccessChangePassword : ChangePasswordActivityState()
    data class Loading(val isLoading: Boolean) : ChangePasswordActivityState()
    data class Error(val throwable: Throwable) : ChangePasswordActivityState()
    data class ErrorChangePassword(val response: WrappedResponse<Nothing>) :
        ChangePasswordActivityState()
}