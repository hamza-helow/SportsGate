package com.souqApp.presentation.forgot_password

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.databinding.FragmentForgotPasswordBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.infra.extension.toValidPhoneNumber
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForgotPasswordFragment :
    BaseFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate),
    View.OnClickListener {

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.isByPhone = viewModel.isByPhone
        observeToLoading()
        initListener()
        observeToValidate()
    }

    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner , ::showLoading)
    }


    private fun observeToValidate() {
        validate()
        viewModel.validateLiveData.observe(viewLifecycleOwner, binding.btnSubmit::setEnabled)
    }

    private fun validate() {
        val credentialId = if (viewModel.isByPhone)
            binding.includePhoneNumber.phoneEdt.text.toString().toValidPhoneNumber()
        else
            binding.emailEdt.text.toString()

        viewModel.validate(credentialId)
    }


    private fun handleOnOtpSentError(response: WrappedResponse<Nothing>) {
        showDialog(response.formattedErrors())
    }

    private fun handleOnOtpSent() {
        navigate(ForgotPasswordFragmentDirections.toVerificationFragment(getMobileNumber()))
    }

    private fun requestOtp() {
        viewModel.requestPasswordReset(getMobileNumber()) { result ->
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

    private fun getMobileNumber(): String {
        val phoneNumber = binding.includePhoneNumber.phoneEdt.text.toString().toValidPhoneNumber()
        val code = "+962"

        return code + phoneNumber
    }

    private fun initListener() {
        binding.includePhoneNumber.phoneEdt.doAfterTextChanged { validate() }
        binding.emailEdt.doAfterTextChanged { validate() }
        binding.btnSubmit.setOnClickListener(this)
    }


    override fun onClick(view: View) {
        when (view.id) {
            binding.btnSubmit.id -> requestOtp()
        }
    }
}
