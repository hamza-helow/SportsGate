package com.souqApp.presentation.create_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.domain.create_password.CreatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePasswordViewModel @Inject constructor(private val createPasswordUseCase: CreatePasswordUseCase) :
    ViewModel() {

    private val _state = MutableLiveData<CreatePasswordActivityState>()
    val state: LiveData<CreatePasswordActivityState> get() = _state

    private fun setLoading(isLoading: Boolean) {
        _state.value = CreatePasswordActivityState.Loading(isLoading)
    }

    private fun onError(throwable: Throwable) {
        _state.value = CreatePasswordActivityState.Error(throwable)
    }

    private fun onCreate(isCreated: Boolean) {
        _state.value = CreatePasswordActivityState.Created(isCreated)
    }

    fun createPassword(newPassword: String, resetToken: String) {
        viewModelScope.launch {
            createPasswordUseCase.resetPassword(newPassword, resetToken)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    onCreate(it)
                }
        }
    }

}

sealed class CreatePasswordActivityState {

    data class Loading(val isLoading: Boolean) : CreatePasswordActivityState()
    data class Error(val throwable: Throwable) : CreatePasswordActivityState()
    data class Created(val isCreated: Boolean) : CreatePasswordActivityState()
}