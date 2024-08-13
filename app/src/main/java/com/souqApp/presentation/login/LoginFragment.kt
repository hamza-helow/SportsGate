package com.souqApp.presentation.login

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.login.remote.dto.LoginRequest
import com.souqApp.databinding.FragmentLoginBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.infra.extension.*
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate),
    View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    @Inject
    lateinit var sharedPrefs: SharedPrefs
    private val viewModel: LoginViewModel by viewModels()

    override fun showAppBar() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToLoading()
        observeToLoginByPhone()
    }

    private fun observeToLoginByPhone() {
        viewModel.loginByPhoneLiveData.observe(viewLifecycleOwner, ::handleLoginByPhone)
    }

    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::handleIsLoading)
    }

    override fun onResume() {
        super.onResume()
        initListener()
    }

    private fun initListener() {
        binding.switchTypeLogin.setOnCheckedChangeListener(this)
        binding.forgetPassBtn.setOnClickListener(this)
        binding.loginBtn.setOnClickListener(this)
        binding.txtSkip.setOnClickListener(this)
        binding.createAccBtn.setOnClickListener(this)
        binding.includePassword.passwordEdt.doAfterTextChanged { validate() }
        binding.includePhoneNumber.phoneEdt.doAfterTextChanged { validate() }
    }

    private fun handleLoginByPhone(enable: Boolean) {
        binding.emailInputLay.isVisible(!enable)
        binding.includePhoneNumber.root.isVisible(enable)
    }

    private fun handleSuccessLogin(userEntity: UserEntity) {
        val requiredVerification = userEntity.verified == 2
        sharedPrefs.saveToken(userEntity.token.orEmpty(), requiredVerification.not())
        sharedPrefs.saveUserInfo(userEntity)
        navigateToMainScreen()
    }


    private fun navigateToMainScreen() {
        findNavController().popBackStack()
    }

    private fun handleErrorLogin(rawResponse: WrappedResponse<UserResponse>) {
        showDialog(rawResponse.message)
    }

    private fun handleIsLoading(isLoading: Boolean) {
        binding.loginBtn.isEnabled = !isLoading
        binding.createAccBtn.isEnabled = !isLoading
        binding.loadingProgressBar.isIndeterminate = isLoading
        binding.loadingProgressBar.isVisible(isLoading)

        if (!isLoading) {
            binding.loadingProgressBar.progress = 0
        }
    }

    private fun getUsernameField(): TextInputEditText {
        return if (!viewModel.isByPhone) binding.emailEdt else binding.includePhoneNumber.phoneEdt
    }

    private fun login() {
        var username = getUsernameField().text.toString().trim()
        val password = binding.includePassword.passwordEdt.text.toString()
        val code = if (viewModel.isByPhone) "+962" else ""

        if (viewModel.isByPhone)
            username = username.toValidPhoneNumber()

        if (validate()) {
            viewModel.login(
                LoginRequest(code + username, password, 0, sharedPrefs.firebaseToken())
            ) { result ->

                when (result) {
                    is BaseResult.Errors -> {
                        handleErrorLogin(result.error)
                    }

                    is BaseResult.Success -> {
                        handleSuccessLogin(result.data)
                    }
                }

            }
        }
    }

    private fun validate(): Boolean {
        resetAllError()
        val username = getUsernameField().text.toString().trim()
        val password = binding.includePassword.passwordEdt.text.toString()

        if (viewModel.isByPhone) {
            if (!username.isPhone()) {
                binding.includePhoneNumber.root.errorBorder()
                return false
            }

        } else {
            if (!username.isEmail()) {
                binding.emailInputLay.activeBorder(requireContext(), false)
                return false
            }
        }
        if (!password.isPasswordValid()) {
            binding.includePassword.passwordInputLay.activeBorder(requireContext(), false)
            return false
        }

        binding.loginBtn.isEnabled = true
        return true
    }

    private fun resetAllError() {
        binding.loginBtn.isEnabled = false
        binding.includePassword.passwordInputLay.activeBorder(requireContext(), true)
        binding.emailInputLay.activeBorder(requireContext(), true)
        binding.includePhoneNumber.root.successBorder()
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.loginBtn.id -> login()
            binding.createAccBtn.id -> goToCreateAccountScreen()
            binding.forgetPassBtn.id -> navigateToForgotPasswordFragment()
            binding.txtSkip.id -> findNavController().popBackStack()
        }
    }

    private fun navigateToForgotPasswordFragment() {
        navigate(LoginFragmentDirections.toForgotPasswordGraph())
    }

    private fun goToCreateAccountScreen() {
        navigate(LoginFragmentDirections.toRegisterGraph())
    }

    private fun loginByPhoneToggle() {
        viewModel.loginByPhoneToggle()
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        loginByPhoneToggle()
        binding.loginBtn.isEnabled = validate()
    }

}