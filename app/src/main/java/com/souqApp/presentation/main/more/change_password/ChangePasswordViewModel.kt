package com.souqApp.presentation.main.more.change_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.change_password.ChangePasswordUseCase
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private val changePasswordUseCase: ChangePasswordUseCase) :
    ViewModel() {

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private fun setLoading(isLoading: Boolean) {
        loadingLiveData.value = isLoading
    }

    fun changePassword(
        oldPassword: String,
        newPassword: String,
        onResult: (BaseResult<EmptyEntity, WrappedResponse<Nothing>>) -> Unit
    ) {
        viewModelScope.launch {
            changePasswordUseCase
                .changePassword(oldPassword, newPassword)
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    onResult(it)
                }
        }
    }

}