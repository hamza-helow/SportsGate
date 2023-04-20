package com.souqApp.presentation.forgot_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.verifcation.VerificationUseCase
import com.souqApp.infra.extension.isPhone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val verificationUseCase: VerificationUseCase) :
    ViewModel() {

    val state: MutableLiveData<ForgotPasswordState> = MutableLiveData()

    fun validate(mobileNumber: String) {
        state.value = ForgotPasswordState.Validate(mobileNumber.isPhone())
    }

    private fun onError(throwable: Throwable) {
        state.value = ForgotPasswordState.Error(throwable)
    }

    private fun showLoading(loading:Boolean){
        state.value = ForgotPasswordState.ShowLoading(loading)
    }

    fun requestPasswordReset(phoneNumber: String) {
        viewModelScope.launch {
            verificationUseCase
                .requestPasswordReset(phoneNumber)
                .onStart {
                    showLoading(true)
                }
                .catch {
                    showLoading(false)
                    onError(it)
                }
                .collect {
                    showLoading(false)
                    when (it) {
                        is BaseResult.Errors -> {
                            state.value = ForgotPasswordState.OtpSentError(it.error)
                        }
                        is BaseResult.Success -> {
                            state.value = ForgotPasswordState.OtpSent
                        }
                    }
                }
        }
    }


}