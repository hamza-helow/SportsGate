package com.souqApp.presentation.forgot_password

import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.souqApp.databinding.FragmentForgotPasswordBinding
import com.souqApp.infra.extension.isPhone
import com.souqApp.infra.extension.toPhoneNumber
import com.souqApp.presentation.base.BaseFragment

class ForgotPasswordFragment :
    BaseFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate),
    View.OnClickListener {

    override fun onResume() {
        super.onResume()
        initListener()
    }

    private fun goToVerificationActivity() {
        val phoneNumber = binding.includePhoneNumber.phoneEdt.text.toString().toPhoneNumber()
        val code = "+962"
        navigate(ForgotPasswordFragmentDirections.toVerificationFragment(code + phoneNumber))
    }

    private fun initListener() {
        binding.includePhoneNumber.phoneEdt.doAfterTextChanged { validate() }
        binding.btnSubmit.setOnClickListener(this)
    }

    private fun validate() {
        binding.btnSubmit.isEnabled = false
        val phoneNumber = binding.includePhoneNumber.phoneEdt.text.toString()

        if (!phoneNumber.isPhone()) {
            return
        }
        binding.btnSubmit.isEnabled = true
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.btnSubmit.id -> goToVerificationActivity()
        }
    }
}
