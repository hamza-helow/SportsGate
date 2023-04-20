package com.souqApp.presentation.login

import android.util.Log
import android.view.View
import android.widget.CompoundButton
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.souqApp.data.common.mapper.toUserResponse
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.login.remote.dto.LoginRequest
import com.souqApp.databinding.FragmentLoginBinding
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.infra.extension.*
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate),
    View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    val viewModel: LoginViewModel by viewModels()

    override fun showAppBar() = false

    override fun onResume() {
        super.onResume()
        viewModel.resetState()
        initListener()
        observe()
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

    private fun observe() {
        viewModel.mState.flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
            .onEach { state -> handleState(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: LoginActivityState) {
        when (state) {
            is LoginActivityState.ShowToast -> handleShowToast(state.message)
            is LoginActivityState.IsLoading -> handleIsLoading(state.isLoading)
            is LoginActivityState.Init -> Unit
            is LoginActivityState.ErrorLogin -> handleErrorLogin(state.rawResponse)
            is LoginActivityState.SuccessLogin -> handleSuccessLogin(state.loginEntity)
            is LoginActivityState.LoginByPhone -> handleLoginByPhone(state.isEnable)
        }
    }

    private fun handleShowToast(message: String) {
        Log.d(tag, message)
    }

    private fun handleLoginByPhone(enable: Boolean) {
        binding.emailInputLay.isVisible(!enable)
        binding.includePhoneNumber.root.isVisible(enable)
    }

    private fun handleSuccessLogin(userEntity: UserEntity) {
        val requiredVerification = userEntity.verified == 2
        sharedPrefs.saveToken(userEntity.token.orEmpty(), requiredVerification.not())
        sharedPrefs.saveUserInfo(userEntity.toUserResponse())
        if (requiredVerification) {
            navigateToVerificationScreen()
        } else
            navigateToMainScreen()
    }

    private fun navigateToVerificationScreen() {
        navigate(LoginFragmentDirections.toVerificationFragment(null))
    }

    private fun navigateToMainScreen() {
        findNavController().popBackStack()
    }

    private fun handleErrorLogin(rawResponse: WrappedResponse<UserResponse>) {
        showErrorDialog(rawResponse.message)
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
        return if (!viewModel.isPhoneEnable) binding.emailEdt else binding.includePhoneNumber.phoneEdt
    }

    private fun login() {
        //  hideKeyboard()
        var username = getUsernameField().text.toString().trim()
        val password = binding.includePassword.passwordEdt.text.toString()
        val code = if (viewModel.isPhoneEnable) "+962" else ""

        if (viewModel.isPhoneEnable)
            username = username.toPhoneNumber()

        if (validate()) {
            viewModel.login(LoginRequest(code + username, password, 0, sharedPrefs.firebaseToken()))
        }
    }

    private fun validate(): Boolean {
        resetAllError()
        val username = getUsernameField().text.toString().trim()
        val password = binding.includePassword.passwordEdt.text.toString()

        if (viewModel.isPhoneEnable) {
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
        navigate(LoginFragmentDirections.toForgotPasswordFragment())
    }

    private fun goToCreateAccountScreen() {
        navigate(LoginFragmentDirections.toRegisterFragment())
    }

    private fun loginByPhoneToggle() {
        viewModel.loginByPhoneToggle()
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        loginByPhoneToggle()
        binding.loginBtn.isEnabled = validate()
    }

}