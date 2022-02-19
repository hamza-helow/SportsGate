package com.souqApp.presentation.forgot_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.databinding.ActivityForgotPasswordBinding
import com.souqApp.infra.extension.isPhone
import com.souqApp.infra.extension.setup
import com.souqApp.infra.extension.start
import com.souqApp.infra.extension.toPhoneNumber
import com.souqApp.infra.utils.PHONE_NUMBER
import com.souqApp.presentation.verification.VerificationActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityForgotPasswordBinding
    val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.includeAppBar.toolbar)
        supportActionBar?.setup()
        initListener()

        viewModel.state.observe(this, { handleState(it) })

    }

    private fun handleState(state: ForgotPasswordActivityState?) {

        when (state) {
            ForgotPasswordActivityState.CodeSent -> onCodeSent()
            is ForgotPasswordActivityState.Error -> onError(state.throwable)
            is ForgotPasswordActivityState.ErrorSendCode -> onErrorSendCode(state.response)
            is ForgotPasswordActivityState.Init -> Unit
            is ForgotPasswordActivityState.Loading -> onLoading(state.isLoading)
        }
    }

    private fun onCodeSent() {

        val phoneNumber = binding.includePhoneNumber.phoneEdt.text.toString().toPhoneNumber()
        val code = "+962"

        val intent = Intent(this, VerificationActivity::class.java)
        intent.putExtra(PHONE_NUMBER, code + phoneNumber)
        startActivity(intent)
    }

    private fun onError(throwable: Throwable) {

        Log.e("ErerR", throwable.stackTraceToString())
    }

    private fun onErrorSendCode(response: WrappedResponse<Nothing>) {

    }

    private fun onLoading(isLoading: Boolean) {
        binding.loader.loadingProgressBar.start(isLoading)
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.btnSubmit.id -> sendOtp()
        }
    }

    private fun sendOtp() {

        val phoneNumber = binding.includePhoneNumber.phoneEdt.text.toString().toPhoneNumber()
        val code = "+962"

        viewModel.requestPasswordReset(code + phoneNumber)

    }
}
