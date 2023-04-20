package com.souqApp.presentation.register

import android.util.Log
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.souqApp.data.common.remote.dto.TokenResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.register.remote.dto.RegisterRequest
import com.souqApp.databinding.FragmentRegisterBinding
import com.souqApp.domain.common.entity.TokenEntity
import com.souqApp.infra.extension.*
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate),
    View.OnClickListener {

    @Inject
    lateinit var sharedPrefs: SharedPrefs
    val viewModel: RegisterViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        viewModel.resetState()
        validate()
        initListeners()
        observe()
    }

    private fun validate() {
        val fullName = binding.includeFullName.emailEdt.text.toString().trim()
        val email = binding.includeEmail.emailEdt.text.toString().trim()
        val phone = binding.includePhoneNumber.phoneEdt.text.toString().toPhoneNumber()
        val password = binding.includePassword.passwordEdt.text.toString()
        val confirmPassword = binding.includeConfirmPassword.passwordEdt.text.toString()
        viewModel.validate(fullName, email, phone, password, confirmPassword)
    }

    private fun observe() {
        viewModel.mState.flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
            .onEach { state -> handleState(state) }
            .launchIn(lifecycleScope)
    }

    private fun initListeners() {
        binding.registerBtn.setOnClickListener(this)
        binding.includeFullName.emailEdt.doAfterTextChanged { validate() }
        binding.includeEmail.emailEdt.doAfterTextChanged { validate() }
        binding.includePhoneNumber.phoneEdt.doAfterTextChanged { validate() }
        binding.includePassword.passwordEdt.doAfterTextChanged { validate() }
        binding.includeConfirmPassword.passwordEdt.doAfterTextChanged { validate() }
    }

    private fun handleState(state: RegisterFragmentState) {
        when (state) {
            is RegisterFragmentState.ShowToast -> handleShowToast(state.message)
            is RegisterFragmentState.IsLoading -> handleLoading(state.isLoading)
            is RegisterFragmentState.ErrorRegister -> handleErrorRegister(state.rawResponse)
            is RegisterFragmentState.SuccessRegister -> handleSuccessRegister(state.tokenEntity)
            is RegisterFragmentState.Init -> Unit
            is RegisterFragmentState.Validate -> handleValidate(state.isValid)
        }
    }

    private fun handleSuccessRegister(tokenEntity: TokenEntity) {
        sharedPrefs.saveToken(tokenEntity.token, isLogin = false)
        navigateToVerificationScreen()
    }

    private fun navigateToVerificationScreen() {
        navigate(RegisterFragmentDirections.toVerificationFragment(null))
    }

    private fun handleErrorRegister(response: WrappedResponse<TokenResponse>) {
        showErrorDialog(response.formattedErrors())
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.registerBtn.isEnabled = !isLoading
        binding.loader.loadingProgressBar.isIndeterminate = isLoading
        binding.loader.loadingProgressBar.isVisible(isLoading)

        if (!isLoading) {
            binding.loader.loadingProgressBar.progress = 0
        }
    }

    private fun handleShowToast(message: String) {
        Log.e(tag, message)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.registerBtn.id -> createAccount()
        }
    }

    private fun createAccount() {
        val fullName = binding.includeFullName.emailEdt.text.toString().trim()
        val email = binding.includeEmail.emailEdt.text.toString().trim()
        val code = "+962"
        val phone = code + binding.includePhoneNumber.phoneEdt.text.toString().toPhoneNumber()
        val password = binding.includePassword.passwordEdt.text.toString()

        viewModel.register(RegisterRequest(fullName, email, phone, password))
    }


    private fun handleValidate(isValid: Boolean) {
        binding.registerBtn.isEnabled = isValid
    }
}