package com.souqApp.presentation.create_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.domain.create_password.CreatePasswordUseCase
import com.souqApp.infra.extension.isPasswordValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePasswordViewModel @Inject constructor(private val createPasswordUseCase: CreatePasswordUseCase) :
    ViewModel() {

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val validateLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private fun setLoading(isLoading: Boolean) {
        loadingLiveData.value = isLoading
    }


    fun validate(password: String, confirmPassword: String) {
        validateLiveData.value = password.isPasswordValid() && password == confirmPassword
    }

    fun createPassword(
        newPassword: String,
        resetToken: String,
        onChanged: (changed: Boolean) -> Unit
    ) {
        viewModelScope.launch {
            createPasswordUseCase.resetPassword(newPassword, resetToken)
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    onChanged(it)
                }
        }
    }

}