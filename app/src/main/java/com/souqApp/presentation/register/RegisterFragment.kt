package com.souqApp.presentation.register

import android.content.Intent
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
import com.souqApp.presentation.verification.VerificationFragment
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
        initListeners()
        observe()
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

    private fun handleState(state: RegisterActivityState) {
        when (state) {
            is RegisterActivityState.ShowToast -> handleShowToast(state.message)
            is RegisterActivityState.IsLoading -> handleLoading(state.isLoading)
            is RegisterActivityState.ErrorRegister -> handleErrorRegister(state.rawResponse)
            is RegisterActivityState.SuccessRegister -> handleSuccessRegister(state.tokenEntity)
            is RegisterActivityState.Init -> Unit
        }
    }

    private fun handleSuccessRegister(tokenEntity: TokenEntity) {
        sharedPrefs.saveToken(tokenEntity.token, isLogin = false)
        navigateToVerificationActivity()
    }

    private fun navigateToVerificationActivity() {
        startActivity(Intent(requireContext(), VerificationFragment::class.java))
    }

    private fun handleErrorRegister(response: WrappedResponse<TokenResponse>) {
        showErrorDialog(response.message)
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


    private fun validate(): Boolean {
        val fullName = binding.includeFullName.emailEdt.text.toString().trim()
        val email = binding.includeEmail.emailEdt.text.toString().trim()
        val phone = binding.includePhoneNumber.phoneEdt.text.toString()
        val password = binding.includePassword.passwordEdt.text.toString()
        val confirmPassword = binding.includeConfirmPassword.passwordEdt.text.toString()

        resetAllError()

        Log.e(tag, phone.toPhoneNumber())

        if (fullName.isEmpty()) {
            binding.includeFullName.emailInputLay.activeBorder(requireContext(), false)
            return false
        }

        if (!email.isEmail()) {
            binding.includeEmail.emailInputLay.activeBorder(requireContext(), false)
            return false
        }

        if (!phone.toPhoneNumber().isPhone()) {
            //  binding.includeFullName.emailInputLay.activeBorder(this, false)
            return false
        }

        if (!password.isPasswordValid()) {
            binding.includePassword.passwordInputLay.activeBorder(requireContext(), false)
            return false
        }

        if (!confirmPassword.isPasswordValid()) {
            binding.includeConfirmPassword.passwordInputLay.activeBorder(requireContext(), false)
            return false
        }

        if (password != confirmPassword) {
            binding.includePassword.passwordInputLay.activeBorder(requireContext(), false)
            binding.includeConfirmPassword.passwordInputLay.activeBorder(requireContext(), false)
            return false
        }

        binding.registerBtn.isEnabled = true
        return true
    }


    private fun resetAllError() {
        binding.registerBtn.isEnabled = false
        binding.includeFullName.emailInputLay.activeBorder(requireContext(), true)
        binding.includeEmail.emailInputLay.activeBorder(requireContext(), true)
        binding.includePassword.passwordInputLay.activeBorder(requireContext(), true)
        binding.includeConfirmPassword.passwordInputLay.activeBorder(requireContext(), true)
    }
}