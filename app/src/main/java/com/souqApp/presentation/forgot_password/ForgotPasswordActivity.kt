package com.souqApp.presentation.forgot_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.souqApp.databinding.ActivityForgotPasswordBinding
import com.souqApp.infra.extension.*
import com.souqApp.infra.utils.PHONE_NUMBER
import com.souqApp.presentation.verification.VerificationActivity

class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.includeAppBar.toolbar)
        supportActionBar?.setup()
        initListener()
    }

    private fun goToVerificationActivity() {
        val phoneNumber = binding.includePhoneNumber.phoneEdt.text.toString().toPhoneNumber()
        val code = "+962"

        val intent = Intent(this, VerificationActivity::class.java)
        intent.putExtra(PHONE_NUMBER, code + phoneNumber)
        startActivity(intent)
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
            binding.btnSubmit.id -> goToVerificationActivity()
        }
    }

}
