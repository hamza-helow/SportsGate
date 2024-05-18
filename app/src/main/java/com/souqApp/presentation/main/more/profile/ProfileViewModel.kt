package com.souqApp.presentation.main.more.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.users.DeleteUserUseCase
import com.souqApp.domain.users.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) :
    ViewModel() {

    private val state = MutableStateFlow<ProfileActivityState>(ProfileActivityState.Init)
    val mState: StateFlow<ProfileActivityState> get() = state


    fun setProfileChanged(isChanged: Boolean) {
        state.value = ProfileActivityState.ProfileChanged(isChanged)
    }

    private fun setLoading() {
        state.value = ProfileActivityState.IsLoading(true)
    }

    private fun hideLoading() {
        state.value = ProfileActivityState.IsLoading(false)
    }

    private fun onAccountDeleted(message: String) {
        state.value = ProfileActivityState.AccountDeleted(message)
    }

    private fun showToast(message: String) {
        state.value = ProfileActivityState.ShowToast(message)
    }


    private fun successUpdateProfile(loginEntity: UserEntity) {
        state.value = ProfileActivityState.SuccessUpdateProfile(loginEntity)
    }

    private fun oneError(message: String) {
        state.value = ProfileActivityState.OnError(message)
    }

    fun updateProfile(name: String, image: String) {
        viewModelScope.launch {
            updateProfileUseCase.invoke(name, image)
                .onStart { setLoading() }
                .catch {
                    hideLoading()
                    showToast(it.stackTraceToString())
                }
                .collect {
                    hideLoading()
                    when (it) {
                        is BaseResult.Success -> successUpdateProfile(it.data)
                        is BaseResult.Errors -> oneError(it.error.message)
                    }
                }
        }
    }


    fun deleteUser(email: String) {
        viewModelScope.launch {
            deleteUserUseCase.invoke(email)
                .onStart { setLoading() }
                .catch { hideLoading() }
                .collect {
                    hideLoading()
                    when (it) {
                        is BaseResult.Errors -> oneError(it.error.message)
                        is BaseResult.Success -> onAccountDeleted(it.message)
                    }
                }
        }
    }

}

sealed class ProfileActivityState {
    object Init : ProfileActivityState()
    data class IsLoading(val isLoading: Boolean) : ProfileActivityState()
    data class ProfileChanged(val isChanged: Boolean) :
        ProfileActivityState()

    data class ShowToast(val message: String) : ProfileActivityState()
    data class SuccessUpdateProfile(val userEntity: UserEntity) : ProfileActivityState()
    data class OnError(val message: String) : ProfileActivityState()

    data class AccountDeleted(val message: String) : ProfileActivityState()
}