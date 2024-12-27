package com.souqApp.presentation.verify_by_method

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.auth.usecase.VerifyMethodUseCase
import com.souqApp.domain.auth.usecase.SendOtpUseCase
import com.souqApp.presentation.common.enums.VerificationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyByMethodViewModel @Inject constructor(
    private val verifyMethodUseCase: VerifyMethodUseCase,
    private val sendOtpUseCase: SendOtpUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val sendOtpLiveData: MutableLiveData<BaseResult<EmptyEntity, WrappedResponse<Nothing>>> =
        MutableLiveData()

    private val verifyType get() = savedStateHandle.get<VerificationType>("verifyType")

    private fun showLoading(loading: Boolean) {
        loadingLiveData.value = loading
    }

    fun sendOtp() {
        viewModelScope.launch {
            sendOtpUseCase.invoke(verifyType == VerificationType.BY_PHONE)
                .onStart {
                    showLoading(true)
                }.catch {
                    showLoading(false)
                }
                .collect { result ->
                    showLoading(false)
                    sendOtpLiveData.value = result
                }
        }

    }


    fun verify(
        code: String,
        onResult: (BaseResult<UserEntity, WrappedResponse<UserResponse>>) -> Unit
    ) {

        viewModelScope.launch {
            verifyMethodUseCase.invoke(
                byMobile = verifyType == VerificationType.BY_PHONE,
                code = code
            ).onStart {
                loadingLiveData.value = true
            }.catch {
                loadingLiveData.value = false
            }.collect { result ->
                loadingLiveData.value = false
                onResult(result)
            }
        }

    }
}