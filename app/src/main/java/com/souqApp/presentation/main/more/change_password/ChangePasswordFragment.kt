package com.souqApp.presentation.main.more.change_password

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.databinding.FragmentChangePasswordBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.infra.extension.activeBorder
import com.souqApp.infra.extension.isPasswordValid
import com.souqApp.infra.extension.showToast
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseFragment<FragmentChangePasswordBinding>(FragmentChangePasswordBinding::inflate),
    View.OnClickListener {

    private val viewModel: ChangePasswordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        observeToLoading()
    }

    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::handleLoading)
    }

    private fun handleLoading(loading: Boolean) {
        showLoading(loading)
    }

    private fun whenErrorChangePassword(response: WrappedResponse<Nothing>) {
        showDialog(response.message)
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
        viewModel.changePassword(currentPassword, newPassword) { result ->
            when (result) {
                is BaseResult.Errors -> whenErrorChangePassword(result.error)
                is BaseResult.Success -> whenSuccessChangePassword()
            }
        }
    }
}