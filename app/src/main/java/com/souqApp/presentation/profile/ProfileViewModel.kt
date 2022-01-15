package com.souqApp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.profile.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileUseCase: ProfileUseCase) :
    ViewModel() {

    private val state = MutableStateFlow<ProfileActivityState>(ProfileActivityState.Init)
    val mState: StateFlow<ProfileActivityState> get() = state

    private fun setLoading() {
        state.value = ProfileActivityState.IsLoading(true)
    }


    private fun hideLoading() {
        state.value = ProfileActivityState.IsLoading(false)
    }


    private fun showToast(message: String) {
        state.value = ProfileActivityState.ShowToast(message)
    }


    private fun successUpdateProfile(loginEntity: UserEntity) {
        state.value = ProfileActivityState.SuccessUpdateProfile(loginEntity)
    }

    private fun errorUpdateProfile(rawResponse: WrappedResponse<UserResponse>) {
        state.value = ProfileActivityState.ErrorUpdateProfile(rawResponse)
    }


    fun updateProfile(name: String, image: String) {
        viewModelScope.launch {
            profileUseCase.updateProfile(name, image)
                .onStart { setLoading() }
                .catch {
                    hideLoading()
                    showToast(it.stackTraceToString())
                }
                .collect {
                    hideLoading()
                    when (it) {
                        is BaseResult.Success -> successUpdateProfile(it.data)
                        is BaseResult.Errors -> errorUpdateProfile(it.error)

                    }
                }


        }
    }

}

sealed class ProfileActivityState {
    object Init : ProfileActivityState()
    data class IsLoading(val isLoading: Boolean) : ProfileActivityState()
    data class ShowToast(val message: String) : ProfileActivityState()
    data class SuccessUpdateProfile(val userEntity: UserEntity) : ProfileActivityState()
    data class ErrorUpdateProfile(val rawResponse: WrappedResponse<UserResponse>) :
        ProfileActivityState()
}