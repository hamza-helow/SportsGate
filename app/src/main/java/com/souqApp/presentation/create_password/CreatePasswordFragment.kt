package com.souqApp.presentation.create_password

import android.util.Log
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.souqApp.R
import com.souqApp.databinding.FragmentCreatePasswordBinding
import com.souqApp.infra.extension.activeBorder
import com.souqApp.infra.extension.isPasswordValid
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePasswordFragment :
    BaseFragment<FragmentCreatePasswordBinding>(FragmentCreatePasswordBinding::inflate),
    View.OnClickListener {

    private val viewModel: CreatePasswordViewModel by viewModels()

    private val args: CreatePasswordFragmentArgs by navArgs()

    override fun onResume() {
        super.onResume()
        viewModel.state.observe(this) { handleState(it) }
        initListener()
    }

    private fun initListener() {
        binding.btnSave.setOnClickListener(this)
        binding.confirmPasswordEdt.doAfterTextChanged { validate() }
        binding.passwordEdt.doAfterTextChanged { validate() }
    }

    private fun handleState(state: CreatePasswordActivityState) {
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
            findNavController().popBackStack(R.id.loginFragment, false)
            requireContext().showToast(getString(R.string.password_changed))
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.btnSave.id -> save()
        }
    }

    private fun save() {
        val password = binding.passwordEdt.text.toString()
        viewModel.createPassword(password, args.resetToken)
    }


    private fun validate() {
        resetAllError()

        val newPassword = binding.passwordEdt.text.toString()
        val confirmNewPassword = binding.confirmPasswordEdt.text.toString()

        if (newPassword.isEmpty() || confirmNewPassword.isEmpty() || newPassword != confirmNewPassword) {
            binding.passwordInputLay.activeBorder(requireContext(), false)
            binding.confirmPasswordInputLay.activeBorder(requireContext(), false)
            return
        }

        if (!newPassword.isPasswordValid()) {
            binding.passwordInputLay.activeBorder(requireContext(), false)
            return
        }

        binding.btnSave.isEnabled = true
    }

    private fun resetAllError() {
        binding.btnSave.isEnabled = false
        binding.passwordInputLay.activeBorder(requireContext(), true)
        binding.confirmPasswordInputLay.activeBorder(requireContext(), true)
    }

}