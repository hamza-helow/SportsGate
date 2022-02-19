package com.souqApp.presentation.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.dto.ActiveAccountRequest
import com.souqApp.data.verifcation.remote.dto.CreateTokenResetPasswordEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.verifcation.VerificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(private val verificationUseCase: VerificationUseCase) :
    ViewModel() {

    private val state = MutableStateFlow<VerificationActivityState>(VerificationActivityState.Init)
    val mState: StateFlow<VerificationActivityState> get() = state

    private fun setLoading() {
        state.value = VerificationActivityState.IsLoading(true)
    }

    private fun hideLoading() {
        state.value = VerificationActivityState.IsLoading(false)
    }

    private fun onError(throwable: Throwable) {
        state.value = VerificationActivityState.Error(throwable)
    }

    private fun successAccountVerification(userEntity: UserEntity) {
        state.value = VerificationActivityState.SuccessAccountVerification(userEntity)
    }

    private fun errorAccountVerification(wrappedResponse: WrappedResponse<UserResponse>) {
        state.value = VerificationActivityState.ErrorAccountVerification(wrappedResponse)
    }

    private fun onSuccessResetVerification(createTokenResetPasswordEntity: CreateTokenResetPasswordEntity) {
        state.value =
            VerificationActivityState.SuccessResetVerification(createTokenResetPasswordEntity)
    }

    private fun onErrorResetVerification(response: WrappedResponse<CreateTokenResetPasswordEntity>) {
        state.value = VerificationActivityState.ErrorResetVerification(response)
    }


    fun activeAccount(activeAccountRequest: ActiveAccountRequest) {
        viewModelScope.launch {
            verificationUseCase.invokeActiveAccount(activeAccountRequest)
                .onStart {
                    setLoading()
                }.catch {
                    hideLoading()
                    onError(it)
                }.collect {
                    hideLoading()
                    when (it) {

                        is BaseResult.Success -> successAccountVerification(it.data)
                        is BaseResult.Errors -> errorAccountVerification(it.error)
                    }
                }
        }
    }

    fun createTokenResetPassword(phone: String, code: String) {
        viewModelScope.launch {
            verificationUseCase.createTokenResetPassword(phone, code)
                .onStart { setLoading() }
                .catch {
                    hideLoading()
                    onError(it)
                }
                .collect {
                    hideLoading()
                    when (it) {
                        is BaseResult.Success -> onSuccessResetVerification(it.data)
                        is BaseResult.Errors -> onErrorResetVerification(it.error)
                    }
                }
        }
    }
}

sealed class VerificationActivityState {
    object Init : VerificationActivityState()
    data class IsLoading(val isLoading: Boolean) : VerificationActivityState()
    data class Error(val throwable: Throwable) : VerificationActivityState()
    data class SuccessAccountVerification(val userEntity: UserEntity) : VerificationActivityState()
    data class ErrorAccountVerification(val wrappedResponse: WrappedResponse<UserResponse>) :
        VerificationActivityState()

    data class SuccessResetVerification(val createTokenResetPasswordEntity: CreateTokenResetPasswordEntity) :
        VerificationActivityState()

    data class ErrorResetVerification(val response: WrappedResponse<CreateTokenResetPasswordEntity>) :
        VerificationActivityState()
}
