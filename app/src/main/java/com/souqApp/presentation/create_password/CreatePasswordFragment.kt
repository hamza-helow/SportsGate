package com.souqApp.presentation.create_password

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.souqApp.R
import com.souqApp.databinding.FragmentCreatePasswordBinding
import com.souqApp.infra.extension.showToast
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePasswordFragment :
    BaseFragment<FragmentCreatePasswordBinding>(FragmentCreatePasswordBinding::inflate),
    View.OnClickListener {

    private val viewModel: CreatePasswordViewModel by viewModels()
    private val args: CreatePasswordFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToLoading()
        observeToValidate()
        initListener()
    }

    private fun observeToValidate() {
        validate()
        viewModel.validateLiveData.observe(viewLifecycleOwner, ::handleValidate)
    }

    private fun observeToLoading() {
        viewModel.validateLiveData.observe(viewLifecycleOwner, ::showLoading)
    }

    private fun initListener() {
        binding.btnSave.setOnClickListener(this)
        binding.confirmPasswordEdt.doAfterTextChanged { validate() }
        binding.passwordEdt.doAfterTextChanged { validate() }
    }


    private fun handleValidate(valid: Boolean) {
        binding.btnSave.isEnabled = valid
    }


    private fun handleOnPasswordChanged(created: Boolean) {
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
        viewModel.createPassword(password, args.resetToken, ::handleOnPasswordChanged)
    }


    private fun validate() {
        val newPassword = binding.passwordEdt.text.toString()
        val confirmNewPassword = binding.confirmPasswordEdt.text.toString()
        viewModel.validate(newPassword, confirmNewPassword)
    }
}