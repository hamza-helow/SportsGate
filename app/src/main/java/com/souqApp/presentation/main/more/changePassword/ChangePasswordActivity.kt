package com.souqApp.presentation.main.more.changePassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.souqApp.R
import com.souqApp.data.change_password.remote.dto.ChangePasswordRequest
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.databinding.ActivityChangePasswordBinding
import com.souqApp.infra.extension.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModel: ChangePasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup tool bar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setup(showTitleEnabled = true)
        supportActionBar?.title = getString(R.string.change_password_str)

        initListener()
        observer()
    }

    private fun observer() {

        lifecycleScope.launchWhenCreated {
            viewModel.mState.collect { handleState(it) }
        }
    }

    private fun handleState(state: ChangePasswordActivityState) {

        when (state) {
            is ChangePasswordActivityState.Init -> Unit
            is ChangePasswordActivityState.SuccessChangePassword -> whenSuccessChangePassword()
            is ChangePasswordActivityState.Error -> handleError(state.throwable)
            is ChangePasswordActivityState.ErrorChangePassword -> whenErrorChangePassword(state.response)
            is ChangePasswordActivityState.Loading -> handleLoading(state.isLoading)
        }
    }

    private fun handleError(throwable: Throwable) {

        Log.e("TAG", throwable.stackTraceToString())

    }

    private fun handleLoading(loading: Boolean) {
        binding.includeLoader.loadingProgressBar.start(loading)
        binding.btnSave.isEnabled = !loading
    }

    private fun whenErrorChangePassword(response: WrappedResponse<Nothing>) {
        showGenericAlertDialog(response.formattedErrors())
    }

    private fun whenSuccessChangePassword() {
        showToast("Password changed successfully")
        finish()
    }

    private fun initListener() {
        binding.includeCurrentPassword.passwordEdt.doAfterTextChanged { validate() }
        binding.includeNewPassword.passwordEdt.doAfterTextChanged { validate() }
        binding.includeConfirmNewPassword.passwordEdt.doAfterTextChanged { validate() }
        binding.btnSave.setOnClickListener(this)
    }

    private fun validate() {
        resetAllError()

        val currentPassword = binding.includeCurrentPassword.passwordEdt.text.toString()
        val newPassword = binding.includeNewPassword.passwordEdt.text.toString()
        val confirmNewPassword = binding.includeConfirmNewPassword.passwordEdt.text.toString()

        if (currentPassword.isEmpty()) {
            binding.includeCurrentPassword.passwordInputLay.activeBorder(this, false)
            return
        }

        if (newPassword.isEmpty() || confirmNewPassword.isEmpty() || newPassword != confirmNewPassword) {
            binding.includeNewPassword.passwordInputLay.activeBorder(this, false)
            binding.includeConfirmNewPassword.passwordInputLay.activeBorder(this, false)
            return
        }

        if (!newPassword.isPasswordValid()) {
            binding.includeNewPassword.passwordInputLay.activeBorder(this, false)
            return
        }

        binding.btnSave.isEnabled = true
    }

    private fun resetAllError() {
        binding.btnSave.isEnabled = false
        binding.includeCurrentPassword.passwordInputLay.activeBorder(this, true)
        binding.includeNewPassword.passwordInputLay.activeBorder(this, true)
        binding.includeConfirmNewPassword.passwordInputLay.activeBorder(this, true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(view: View) {
        when (view.id) {

            binding.btnSave.id -> changePassword()
        }
    }

    private fun changePassword() {
        val currentPassword = binding.includeCurrentPassword.passwordEdt.text.toString()
        val newPassword = binding.includeNewPassword.passwordEdt.text.toString()
        viewModel.changePassword(ChangePasswordRequest(currentPassword, newPassword))
    }
}