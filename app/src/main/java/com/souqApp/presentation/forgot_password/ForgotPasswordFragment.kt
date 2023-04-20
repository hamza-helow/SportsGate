package com.souqApp.presentation.forgot_password

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.databinding.FragmentForgotPasswordBinding
import com.souqApp.infra.extension.toPhoneNumber
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
        validate()
        observeToState()

    }

    private fun validate() {
        viewModel.validate(binding.includePhoneNumber.phoneEdt.text.toString().toPhoneNumber())
    }

    private fun observeToState() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ForgotPasswordState.Validate -> {
                    binding.btnSubmit.isEnabled = it.isValid
                }
                ForgotPasswordState.OtpSent -> handleOnOtpSent()
                is ForgotPasswordState.Error -> Unit
                is ForgotPasswordState.OtpSentError -> handleOnOtpSentError(it.response)
                is ForgotPasswordState.ShowLoading -> showLoading(it.loading)
            }
        }
    }

    private fun handleOnOtpSentError(response: WrappedResponse<Nothing>) {
        showErrorDialog(response.formattedErrors())
    }

    private fun handleOnOtpSent() {
        navigate(ForgotPasswordFragmentDirections.toVerificationFragment(getMobileNumber()))
    }

    private fun requestOtp() {
        viewModel.requestPasswordReset(getMobileNumber())
    }

    private fun getMobileNumber(): String {
        val phoneNumber = binding.includePhoneNumber.phoneEdt.text.toString().toPhoneNumber()
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
