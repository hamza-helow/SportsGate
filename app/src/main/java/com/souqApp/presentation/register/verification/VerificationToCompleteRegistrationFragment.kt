package com.souqApp.presentation.register.verification

import androidx.fragment.app.viewModels
import com.souqApp.databinding.FragmentVerificationToCompleteRegistrationBinding
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VerificationToCompleteRegistrationFragment :
    BaseFragment<FragmentVerificationToCompleteRegistrationBinding>(
        FragmentVerificationToCompleteRegistrationBinding::inflate
    ) {

    val viewModel: VerificationToCompleteRegistrationViewModel by viewModels()

}