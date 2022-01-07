package com.souqApp.presentation.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.souqApp.R
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.login.remote.dto.LoginRequest
import com.souqApp.databinding.ActivityLoginBinding
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.infra.extension.isEmail
import com.souqApp.infra.extension.isPasswordValid
import com.souqApp.infra.extension.isPhone
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.utils.SharedPrefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    val viewModel: LoginViewModel by viewModels()

    private val tag: String = LoginActivity::class.java.simpleName

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
        observe()
    }

    private fun initListener() {
        binding.btnSwitchPhoneEmail.setOnClickListener(this)
        binding.loginBtn.setOnClickListener(this)
    }

    private fun observe() {
        viewModel.mState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { stete -> handleState(stete) }
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
        binding.btnSwitchPhoneEmail.icon = AppCompatResources.getDrawable(
            this,
            if (enable) R.drawable.phone_email_switch else R.drawable.email_phone_switch
        )

        binding.emailInputLay.isVisible(!enable)
        binding.phoneInputLay.isVisible(enable)
        binding.flCountryCodes.isVisible(enable)
    }

    private fun handleSuccessLogin(loginEntity: UserEntity) {

    }

    private fun handleErrorLogin(rawResponse: WrappedResponse<UserResponse>) {
        if (rawResponse.errors != null && rawResponse.errors!!.isNotEmpty()) {
            val error = rawResponse.errors!![0]
        }

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
        return if (!viewModel.isPhoneEnable) binding.emailEdt else binding.phoneEdt
    }

    private fun login() {
        val username = getUsernameField().text.toString().trim()
        val password = binding.passwordEdt.text.toString()
        val code = if (viewModel.isPhoneEnable) "+962" else ""

        if (validate(username, password)) {

            viewModel.login(LoginRequest(code + username, password, 0, ""))
        }
    }

    private fun validate(username: String, password: String): Boolean {
        resetAllError()

        if (viewModel.isPhoneEnable) {
            if (!username.isPhone()) {
                setPhoneError(getString(R.string.error_phone_not_valid))
                return false
            }

        } else {
            if (!username.isEmail()) {
                setEmailError(getString(R.string.error_email_not_valid))
                return false
            }
        }

        if (!password.isPasswordValid()) {
            setPasswordError(getString(R.string.error_password_not_valid))
            return false
        }

        return true
    }

    private fun setEmailError(e: String?) {
        binding.emailEdt.error = e
    }

    private fun setPhoneError(e: String?) {
        binding.phoneEdt.error = e
    }

    private fun setPasswordError(e: String?) {
        binding.passwordEdt.error = e
    }

    private fun resetAllError() {
        setEmailError(null)
        setPasswordError(null)
        setPhoneError(null)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.btnSwitchPhoneEmail.id -> loginByPhoneToggle()
            binding.loginBtn.id -> login()
        }
    }

    private fun loginByPhoneToggle() {
        viewModel.loginByPhoneToggle()
    }
}