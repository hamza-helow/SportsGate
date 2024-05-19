package com.souqApp.presentation.verification

import androidx.lifecycle.MutableLiveData
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

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()


    private fun setLoading() {
        loadingLiveData.value = true
    }

    private fun hideLoading() {
        loadingLiveData.value = false
    }


    fun activeAccount(
        activeAccountRequest: ActiveAccountRequest,
        onResult: (BaseResult<UserEntity, WrappedResponse<UserResponse>>) -> Unit
    ) {
        viewModelScope.launch {
            verificationUseCase.invokeActiveAccount(activeAccountRequest)
                .onStart { setLoading() }
                .catch {
                    hideLoading()
                }.collect {
                    hideLoading()
                    onResult(it)
                }
        }
    }

    fun createTokenResetPassword(
        phone: String,
        code: String,
        onResult: (BaseResult<CreateTokenResetPasswordEntity, WrappedResponse<CreateTokenResetPasswordEntity>>) -> Unit
    ) {
        viewModelScope.launch {
            verificationUseCase.createTokenResetPassword(phone, code)
                .onStart { setLoading() }
                .catch { hideLoading() }
                .collect {
                    hideLoading()
                    onResult(it)
                }
        }
    }

    fun requestPasswordReset(phoneNumber: String) {
        viewModelScope.launch {
            verificationUseCase
                .requestPasswordReset(phoneNumber)
                .catch {}
                .collect()
        }
    }

    fun resendActivationCode() {
        viewModelScope.launch {
            verificationUseCase
                .resendActivationCode()
                .catch { }
                .collect()
        }
    }
}