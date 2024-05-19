package com.souqApp.presentation.forgot_password

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.databinding.FragmentForgotPasswordBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.infra.extension.toValidPhoneNumber
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment :
    BaseFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate),
    View.OnClickListener {

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        initListener()
        observeToValidate()
    }

    private fun observeToValidate() {
        validate()
        viewModel.validateLiveData.observe(viewLifecycleOwner, binding.btnSubmit::setEnabled)
    }

    private fun validate() {
        viewModel.validate(binding.includePhoneNumber.phoneEdt.text.toString().toValidPhoneNumber())
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
        binding.btnSubmit.setOnClickListener(this)
    }


    override fun onClick(view: View) {
        when (view.id) {
            binding.btnSubmit.id -> requestOtp()
        }
    }
}
