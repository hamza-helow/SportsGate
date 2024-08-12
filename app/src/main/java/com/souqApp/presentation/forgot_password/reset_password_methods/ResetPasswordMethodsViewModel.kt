package com.souqApp.presentation.forgot_password.reset_password_methods

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.verifcation.GetSupportedPasswordMethodsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordMethodsViewModel @Inject constructor(private val getSupportedPasswordMethodsUseCase: GetSupportedPasswordMethodsUseCase) :
    ViewModel() {

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val methodsLiveData: MutableLiveData<List<String>> = MutableLiveData()

    init {
        getSupportedPasswordMethods()
    }

    private fun getSupportedPasswordMethods() {
        viewModelScope.launch {
            getSupportedPasswordMethodsUseCase.invoke()
                .onStart {
                    loadingLiveData.value = true
                }
                .collect { result ->
                    loadingLiveData.value = false
                    when (result) {
                        is BaseResult.Errors -> Unit
                        is BaseResult.Success -> {
                            methodsLiveData.value = result.data
                        }
                    }
                }
        }
    }

}