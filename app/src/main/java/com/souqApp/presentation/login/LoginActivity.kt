package com.souqApp.presentation.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.login.remote.dto.LoginRequest
import com.souqApp.databinding.ActivityLoginBinding
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.infra.extension.*
import com.souqApp.infra.utils.SharedPrefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener, TextWatcher,
    CompoundButton.OnCheckedChangeListener {

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

        binding.switchTypeLogin.setOnCheckedChangeListener(this)

        binding.loginBtn.setOnClickListener(this)

        binding.passwordEdt.addTextChangedListener(this)
        binding.phoneEdt.addTextChangedListener(this)
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
        binding.emailInputLay.isVisible(!enable)
        binding.layoutPhoneNumber.isVisible(enable)
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

        if (validate()) {

            viewModel.login(LoginRequest(code + username, password, 0, ""))
        }
    }

    private fun validate(): Boolean {
        resetAllError()
        val username = getUsernameField().text.toString().trim()
        val password = binding.passwordEdt.text.toString()

        binding.loginBtn.isEnabled = false

        if (viewModel.isPhoneEnable) {
            if (!username.isPhone()) {
                binding.phoneInputLay.activeBorder(this, false)
                return false
            }

        } else {
            if (!username.isEmail()) {
                binding.emailInputLay.activeBorder(this, false)
                return false
            }
        }

        if (!password.isPasswordValid()) {
            binding.passwordInputLay.activeBorder(this, false)

            return false
        }
        binding.loginBtn.isEnabled = true

        return true
    }

    private fun resetAllError() {
        binding.passwordInputLay.activeBorder(this, true)
        binding.emailInputLay.activeBorder(this, true)
        binding.phoneInputLay.activeBorder(this, true)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.loginBtn.id -> login()
        }
    }

    private fun loginByPhoneToggle() {
        viewModel.loginByPhoneToggle()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(et: Editable?) {
        validate()
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        loginByPhoneToggle()
        validate()
    }
}