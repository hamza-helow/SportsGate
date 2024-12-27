package com.souqApp.presentation.register.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.auth.dto.RegisterRequest
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.auth.usecase.RegisterUseCase
import com.souqApp.infra.extension.isEmail
import com.souqApp.infra.extension.isPasswordValid
import com.souqApp.infra.extension.isPhone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) :
    ViewModel() {

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val validateLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun validate(
        fullName: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String,
        checkAgreeTerms: Boolean
    ) {
        validateLiveData.value = fullName.isNotBlank() && email.isEmail() &&
                phone.isPhone() && password.isPasswordValid() &&
                checkAgreeTerms && password == confirmPassword
    }


    private fun setLoading() {
        loadingLiveData.value = true
    }

    private fun hideLoading() {
        loadingLiveData.value = false
    }

    fun register(
        registerRequest: RegisterRequest,
        onResult: (BaseResult<UserEntity, WrappedResponse<UserResponse>>) -> Unit
    ) {
        viewModelScope.launch {
            registerUseCase.invokeRegister(registerRequest)
                .onStart { setLoading() }
                .catch { hideLoading() }
                .collect {
                    hideLoading()
                    onResult(it)
                }
        }
    }

}