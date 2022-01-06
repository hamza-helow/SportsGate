package com.souqApp.presentation.login

import androidx.lifecycle.ViewModel
import com.souqApp.domain.login.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

}