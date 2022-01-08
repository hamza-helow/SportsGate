package com.souqApp.presentation.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.dto.ActiveAccountRequest
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

    private fun showToast(message: String) {
        state.value = VerificationActivityState.ShowToast(message)
    }

    private fun successVerification(userEntity: UserEntity) {
        state.value = VerificationActivityState.SuccessVerification(userEntity)
    }

    private fun errorVerification(wrappedResponse: WrappedResponse<UserResponse>) {
        state.value = VerificationActivityState.ErrorVerification(wrappedResponse)
    }


    fun activeAccount(activeAccountRequest: ActiveAccountRequest) {
        viewModelScope.launch {
            verificationUseCase.invokeActiveAccount(activeAccountRequest)
                .onStart {
                    setLoading()
                }.catch {
                    hideLoading()
                    showToast(it.stackTraceToString())
                }.collect {
                    hideLoading()
                    when (it) {

                        is BaseResult.Success -> successVerification(it.data)
                        is BaseResult.Errors -> errorVerification(it.error)
                    }
                }
        }
    }
}

sealed class VerificationActivityState {
    object Init : VerificationActivityState()
    data class IsLoading(val isLoading: Boolean) : VerificationActivityState()
    data class ShowToast(val message: String) : VerificationActivityState()
    data class SuccessVerification(val userEntity: UserEntity) : VerificationActivityState()
    data class ErrorVerification(val wrappedResponse: WrappedResponse<UserResponse>) :
        VerificationActivityState()
}
