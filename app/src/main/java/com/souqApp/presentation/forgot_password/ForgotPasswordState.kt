package com.souqApp.presentation.forgot_password

import com.souqApp.data.common.utlis.WrappedResponse

sealed class ForgotPasswordState  {

    data class Validate(val isValid: Boolean) : ForgotPasswordState()
    object OtpSent : ForgotPasswordState()
    data class OtpSentError(val response: WrappedResponse<Nothing>) : ForgotPasswordState()
    data class Error(val throwable: Throwable) : ForgotPasswordState()

    data class ShowLoading(val loading: Boolean) : ForgotPasswordState()



}