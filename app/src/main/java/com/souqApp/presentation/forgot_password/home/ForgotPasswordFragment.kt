package com.souqApp.presentation.forgot_password.home

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.souqApp.NavGraphDirections
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.databinding.FragmentForgotPasswordBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.infra.extension.toValidPhoneNumber
import com.souqApp.presentation.base.BaseFragment
import com.souqApp.presentation.common.enums.VerificationType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment :
    BaseFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate),
    View.OnClickListener {

    private val viewModel: ForgotPasswordViewModel by viewModels()
    private val args: ForgotPasswordFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.isByPhone = args.byPhone
        observeToLoading()
        initListener()
        observeToValidate()
    }

    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::showLoading)
    }


    private fun observeToValidate() {
        validate()
        viewModel.validateLiveData.observe(viewLifecycleOwner, binding.btnSubmit::setEnabled)
    }

    private fun validate() {
        viewModel.validate(args.byPhone, getIdCredential())
    }


    private fun handleOnOtpSentError(response: WrappedResponse<Nothing>) {
        showDialog(response.formattedErrors())
    }

    private fun handleOnOtpSent() {
        navigate(
            NavGraphDirections.toVerificationFragment(
                getIdCredential(),
                if (args.byPhone) VerificationType.RESET_PASSWORD_BY_PHONE else VerificationType.RESET_PASSWORD_BY_EMAIL
            )
        )
    }

    private fun requestOtp() {
        viewModel.requestPasswordReset(args.byPhone, getIdCredential()) { result ->
            when (result) {
                is BaseResult.Errors -> {
                    handleOnOtpSentError(result.error)
                }

                is BaseResult.Success -> {
                    handleOnOtpSent()
                }
            }
        }
    }

    private fun getIdCredential(): String {
        if (args.byPhone) {
            val phoneNumber =
                binding.includePhoneNumber.phoneEdt.text.toString().toValidPhoneNumber()
            val code = "962"
            return code + phoneNumber
        } else {
            return binding.etEmail.text.toString()
        }
    }

    private fun initListener() {
        binding.includePhoneNumber.phoneEdt.doAfterTextChanged { validate() }
        binding.etEmail.doAfterTextChanged { validate() }
        binding.btnSubmit.setOnClickListener(this)
    }


    override fun onClick(view: View) {
        when (view.id) {
            binding.btnSubmit.id -> requestOtp()
        }
    }
}
