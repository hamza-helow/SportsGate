package com.souqApp.presentation.forgot_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
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


    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val validateLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun validate(mobileNumber: String) {
        validateLiveData.value = mobileNumber.isPhone()
    }

    private fun showLoading(loading: Boolean) {
        loadingLiveData.value = loading
    }

    fun requestPasswordReset(
        phoneNumber: String,
        onResult: (BaseResult<EmptyEntity, WrappedResponse<Nothing>>) -> Unit
    ) {
        viewModelScope.launch {
            verificationUseCase
                .requestPasswordReset(phoneNumber)
                .onStart { showLoading(true) }
                .catch { showLoading(false) }
                .collect {
                    showLoading(false)
                    onResult(it)
                }
        }
    }


}