package com.souqApp.presentation.create_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.souqApp.R
import com.souqApp.databinding.ActivityCreatePasswordBinding
import com.souqApp.infra.extension.activeBorder
import com.souqApp.infra.extension.isPasswordValid
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.infra.utils.RESET_TOKEN
import com.souqApp.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePasswordActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityCreatePasswordBinding
    private val viewModel: CreatePasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.state.observe(this, { handleState(it) })
        initListener()
    }

    private fun initListener() {
        binding.btnSave.setOnClickListener(this)
        binding.confirmPasswordEdt.doAfterTextChanged { validate() }
        binding.passwordEdt.doAfterTextChanged { validate() }
    }

    private fun handleState(state: CreatePasswordActivityState?) {
        when (state) {
            is CreatePasswordActivityState.Created -> onCreatePassword(state.isCreated)
            is CreatePasswordActivityState.Error -> onError(state.throwable)
            is CreatePasswordActivityState.Loading -> onLoading(state.isLoading)
        }
    }

    private fun onLoading(loading: Boolean) {
        binding.includeLoader.loadingProgressBar.start(loading)
    }

    private fun onError(throwable: Throwable) {
        Log.e(APP_TAG, throwable.stackTraceToString())
    }

    private fun onCreatePassword(created: Boolean) {
        if (created) {
            val i = Intent(this, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)

            showToast(getString(R.string.password_changed))
        }

    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.btnSave.id -> save()
        }
    }

    private fun save() {
        val password = binding.passwordEdt.text.toString()
        val resetToken = intent.getStringExtra(RESET_TOKEN) ?: ""
        viewModel.createPassword(password, resetToken)
    }


    private fun validate() {
        resetAllError()

        val newPassword = binding.passwordEdt.text.toString()
        val confirmNewPassword = binding.confirmPasswordEdt.text.toString()

        if (newPassword.isEmpty() || confirmNewPassword.isEmpty() || newPassword != confirmNewPassword) {
            binding.passwordInputLay.activeBorder(this, false)
            binding.confirmPasswordInputLay.activeBorder(this, false)
            return
        }

        if (!newPassword.isPasswordValid()) {
            binding.passwordInputLay.activeBorder(this, false)
            return
        }

        binding.btnSave.isEnabled = true
    }

    private fun resetAllError() {
        binding.btnSave.isEnabled = false
        binding.passwordInputLay.activeBorder(this, true)
        binding.confirmPasswordInputLay.activeBorder(this, true)
    }

}