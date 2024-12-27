package com.souqApp.presentation.main.more.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.EmptyEntity
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.domain.auth.usecase.DeleteUserUseCase
import com.souqApp.domain.auth.usecase.SendOtpUseCase
import com.souqApp.domain.auth.usecase.UpdateProfileUseCase
import com.souqApp.presentation.common.enums.VerificationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val sendOtpUseCase: SendOtpUseCase,
) :
    ViewModel() {


    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val profileChangesLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun setProfileChanged(isChanged: Boolean) {
        profileChangesLiveData.value = isChanged
    }

    private fun setLoading() {
        loadingLiveData.value = true
    }

    private fun hideLoading() {
        loadingLiveData.value = false
    }

    fun sendOtpToVerifyMethod(
        verifyType: VerificationType,
        onCollect: (BaseResult<EmptyEntity, WrappedResponse<Nothing>>) -> Unit
    ) {
        viewModelScope.launch {
            sendOtpUseCase.invoke(verifyType == VerificationType.BY_PHONE)
                .onStart {
                    setLoading()
                }.catch {
                    hideLoading()
                }
                .collect { result ->
                    hideLoading()
                    onCollect(result)
                }
        }

    }

    fun updateProfile(
        name: String,
        image: String,
        onResult: (BaseResult<UserEntity, WrappedResponse<UserResponse>>) -> Unit
    ) {
        viewModelScope.launch {
            updateProfileUseCase.invoke(name, image)
                .onStart { setLoading() }
                .catch { hideLoading() }
                .collect {
                    hideLoading()
                    onResult(it)
                }
        }
    }


    fun deleteUser(
        email: String,
        onResult: (BaseResult<List<Any>, WrappedListResponse<Any>>) -> Unit
    ) {
        viewModelScope.launch {
            deleteUserUseCase.invoke(email)
                .onStart { setLoading() }
                .catch { hideLoading() }
                .collect {
                    hideLoading()
                    onResult(it)
                }
        }
    }

}