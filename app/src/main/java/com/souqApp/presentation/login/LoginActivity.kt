package com.souqApp.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.souqApp.data.common.mapper.toUserResponse
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.login.remote.dto.LoginRequest
import com.souqApp.databinding.ActivityLoginBinding
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.infra.extension.*
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.forgot_password.ForgotPasswordActivity
import com.souqApp.presentation.register.RegisterActivity
import com.souqApp.presentation.verification.VerificationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    @Inject
    lateinit var sharedPrefs: SharedPrefs
    private lateinit var binding: ActivityLoginBinding
    val viewModel: LoginViewModel by viewModels()
    private val tag: String = LoginActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        viewModel.mState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
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
        sharedPrefs.saveToken(userEntity.token ?: "")
        sharedPrefs.saveUserInfo(userEntity.toUserResponse())
        if (userEntity.verified == 2) {
            sharedPrefs.isNeedVerify(true)
            navigateToVerificationActivity()
        } else
            navigateToMainActivity()
    }

    private fun navigateToVerificationActivity() {
        startActivity(Intent(this, VerificationActivity::class.java))
    }

    private fun navigateToMainActivity() {
        finish()
    }

    private fun handleErrorLogin(rawResponse: WrappedResponse<UserResponse>) {
        showGenericAlertDialog(rawResponse.formattedErrors())
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
        hideKeyboard()
        var username = getUsernameField().text.toString().trim()
        val password = binding.includePassword.passwordEdt.text.toString()
        val code = if (viewModel.isPhoneEnable) "+962" else ""

        if (viewModel.isPhoneEnable)
            username = username.toPhoneNumber()

        if (validate()) {
            viewModel.login(LoginRequest(code + username, password, 0, ""))
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
                binding.emailInputLay.activeBorder(this, false)
                return false
            }
        }
        if (!password.isPasswordValid()) {
            binding.includePassword.passwordInputLay.activeBorder(this, false)
            return false
        }

        binding.loginBtn.isEnabled = true
        return true
    }

    private fun resetAllError() {
        binding.loginBtn.isEnabled = false
        binding.includePassword.passwordInputLay.activeBorder(this, true)
        binding.emailInputLay.activeBorder(this, true)
        binding.includePhoneNumber.root.successBorder()
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.loginBtn.id -> login()
            binding.createAccBtn.id -> goToCreateAccountScreen()
            binding.forgetPassBtn.id -> goToForgetPasswordActivity()
            binding.txtSkip.id -> finish() // return to MainActivity
        }
    }

    private fun goToForgetPasswordActivity() {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }

    private fun goToCreateAccountScreen() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun loginByPhoneToggle() {
        viewModel.loginByPhoneToggle()
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        loginByPhoneToggle()
        binding.loginBtn.isEnabled = validate()
    }
}