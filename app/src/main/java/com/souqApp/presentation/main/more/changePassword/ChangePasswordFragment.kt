package com.souqApp.presentation.main.more.changePassword

import android.util.Log
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.databinding.FragmentChangePasswordBinding
import com.souqApp.infra.extension.activeBorder
import com.souqApp.infra.extension.isPasswordValid
import com.souqApp.infra.extension.showToast
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>(FragmentChangePasswordBinding::inflate), View.OnClickListener {

    private val viewModel: ChangePasswordViewModel by viewModels()

    override fun onResume() {
        super.onResume()
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
        showLoading(loading)
    }

    private fun whenErrorChangePassword(response: WrappedResponse<Nothing>) {
        showErrorDialog(response.message)
    }

    private fun whenSuccessChangePassword() {
       requireContext().showToast("Password changed successfully")
        findNavController().popBackStack()
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
            binding.includeCurrentPassword.passwordInputLay.activeBorder(requireContext(), false)
            return
        }

        if (newPassword.isEmpty() || confirmNewPassword.isEmpty() || newPassword != confirmNewPassword) {
            binding.includeNewPassword.passwordInputLay.activeBorder(requireContext(), false)
            binding.includeConfirmNewPassword.passwordInputLay.activeBorder(requireContext(), false)
            return
        }

        if (!newPassword.isPasswordValid()) {
            binding.includeNewPassword.passwordInputLay.activeBorder(requireContext(), false)
            return
        }

        binding.btnSave.isEnabled = true
    }

    private fun resetAllError() {
        binding.btnSave.isEnabled = false
        binding.includeCurrentPassword.passwordInputLay.activeBorder(requireContext(), true)
        binding.includeNewPassword.passwordInputLay.activeBorder(requireContext(), true)
        binding.includeConfirmNewPassword.passwordInputLay.activeBorder(requireContext(), true)
    }


    override fun onClick(view: View) {
        when (view.id) {
            binding.btnSave.id -> changePassword()
        }
    }

    private fun changePassword() {
        val currentPassword = binding.includeCurrentPassword.passwordEdt.text.toString()
        val newPassword = binding.includeNewPassword.passwordEdt.text.toString()
        viewModel.changePassword(currentPassword, newPassword)
    }
}