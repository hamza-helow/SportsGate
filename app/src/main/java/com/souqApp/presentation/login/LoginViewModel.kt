package com.souqApp.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.login.remote.dto.LoginRequest
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.login.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val loginByPhoneLiveData: MutableLiveData<Boolean> = MutableLiveData(true)

    val isByPhone get() = loginByPhoneLiveData.value == true

    fun loginByPhoneToggle() {
        loginByPhoneLiveData.value = isByPhone.not()
    }

    private fun setLoading() {
        loadingLiveData.value = true
    }

    private fun hideLoading() {
        loadingLiveData.value = false
    }

    fun login(
        loginRequest: LoginRequest,
        onResult: (BaseResult<UserEntity, WrappedResponse<UserResponse>>) -> Unit
    ) {
        viewModelScope.launch {
            loginUseCase.invoke(loginRequest).onStart { setLoading() }
                .catch { hideLoading() }
                .collect { result ->
                    hideLoading()
                    onResult(result)
                }
        }
    }

}
