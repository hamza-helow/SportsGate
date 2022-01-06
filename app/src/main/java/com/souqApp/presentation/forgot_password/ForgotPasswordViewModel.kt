package com.souqApp.presentation.forgot_password

import androidx.lifecycle.ViewModel
import com.souqApp.domain.forgot_password.ForgotPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val forgotPasswordUseCase: ForgotPasswordUseCase) :
    ViewModel() {

}