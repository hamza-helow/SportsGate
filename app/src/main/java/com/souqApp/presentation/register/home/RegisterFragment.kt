package com.souqApp.presentation.register.home

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.souqApp.R
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.auth.dto.RegisterRequest
import com.souqApp.databinding.FragmentRegisterBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.infra.extension.toValidPhoneNumber
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate),
    View.OnClickListener {

    @Inject
    lateinit var sharedPrefs: SharedPrefs
    val viewModel: RegisterViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        observeToLoading()
        observeToValidate()
        validate()
        initListeners()
    }

    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::handleLoading)
    }

    private fun observeToValidate() {
        validate()
        viewModel.validateLiveData.observe(viewLifecycleOwner, ::handleValidate)
    }

    private fun validate() {
        val fullName = binding.fullNameEdt.text.toString().trim()
        val email = binding.emailEdt.text.toString().trim()
        val phone = binding.includePhoneNumber.phoneEdt.text.toString().toValidPhoneNumber()
        val password = binding.passwordEdt.text.toString()
        val confirmPassword = binding.confirmPasswordEdt.text.toString()
        val isAgree = binding.checkBoxAgree.isChecked
        viewModel.validate(fullName, email, phone, password, confirmPassword, isAgree)
    }


    private fun initListeners() {
        binding.txtTermsAndConditions.setOnClickListener(this)
        binding.registerBtn.setOnClickListener(this)
        binding.fullNameEdt.doAfterTextChanged { validate() }
        binding.emailEdt.doAfterTextChanged { validate() }
        binding.includePhoneNumber.phoneEdt.doAfterTextChanged { validate() }
        binding.passwordEdt.doAfterTextChanged { validate() }
        binding.confirmPasswordEdt.doAfterTextChanged { validate() }
        binding.checkBoxAgree.setOnCheckedChangeListener { _, _ -> validate() }
    }

    private fun handleSuccessRegister(userEntity: UserEntity) {
        sharedPrefs.saveUserInfo(userEntity)
        findNavController().popBackStack(R.id.homeFragment, false)
    }

    private fun handleErrorRegister(response: WrappedResponse<UserResponse>) {
        showDialog(response.formattedErrors())
    }

    private fun handleLoading(isLoading: Boolean) {
        showLoading(isLoading)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.registerBtn.id -> createAccount()
            binding.txtTermsAndConditions.id -> navigateToTermsAndConditionScreen()
        }
    }

    private fun navigateToTermsAndConditionScreen() {
        navigate(RegisterFragmentDirections.toTermsAndConditionsFragment())
    }

    private fun createAccount() {
        val fullName = binding.fullNameEdt.text.toString().trim()
        val email = binding.emailEdt.text.toString().trim()
        val code = "+962"
        val phone = code + binding.includePhoneNumber.phoneEdt.text.toString().toValidPhoneNumber()
        val password = binding.passwordEdt.text.toString()
        viewModel.register(RegisterRequest(fullName, email, phone, password)) { result ->
            when (result) {
                is BaseResult.Errors -> handleErrorRegister(result.error)
                is BaseResult.Success -> handleSuccessRegister(result.data)
            }
        }
    }


    private fun handleValidate(isValid: Boolean) {
        binding.registerBtn.isEnabled = isValid
    }
}