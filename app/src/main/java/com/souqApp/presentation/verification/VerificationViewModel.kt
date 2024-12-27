package com.souqApp.presentation.verification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.dto.CreateTokenResetPasswordEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.verifcation.usecase.CreateResetTokenUseCase
import com.souqApp.domain.verifcation.usecase.RequestPasswordResetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val createResetTokenUseCase: CreateResetTokenUseCase,
    private val requestPasswordResetUseCase: RequestPasswordResetUseCase
) :
    ViewModel() {

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private fun setLoading() {
        loadingLiveData.value = true
    }

    private fun hideLoading() {
        loadingLiveData.value = false
    }

    fun createTokenResetPassword(
        byPhone: Boolean,
        credentialId: String,
        code: String,
        onResult: (BaseResult<CreateTokenResetPasswordEntity, WrappedResponse<CreateTokenResetPasswordEntity>>) -> Unit
    ) {
        viewModelScope.launch {
            createResetTokenUseCase.invoke(byPhone, credentialId, code)
                .onStart { setLoading() }
                .catch { hideLoading() }
                .collect {
                    hideLoading()
                    onResult(it)
                }
        }
    }

    fun requestPasswordReset(credentialId: String, isPhone: Boolean) {
        viewModelScope.launch {
            requestPasswordResetUseCase
                .invoke(credentialId, isPhone)
                .catch {}
                .collect()
        }
    }
}