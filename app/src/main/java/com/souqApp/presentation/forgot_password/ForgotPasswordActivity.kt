package com.souqApp.presentation.forgot_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.souqApp.databinding.ActivityForgotPasswordBinding
import com.souqApp.infra.extension.isPhone
import com.souqApp.infra.extension.setup
import com.souqApp.infra.extension.toPhoneNumber
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


    }
}
