package com.souqApp.presentation.verification

import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.souqApp.R
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.dto.ActiveAccountRequest
import com.souqApp.data.verifcation.remote.dto.CreateTokenResetPasswordEntity
import com.souqApp.databinding.FragmentVerificationBinding
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class VerificationFragment :
    BaseFragment<FragmentVerificationBinding>(FragmentVerificationBinding::inflate),
    View.OnClickListener {

    @Inject
    lateinit var sharedPrefs: SharedPrefs
    private val viewModel: VerificationViewModel by viewModels()
    private val args: VerificationFragmentArgs by navArgs()

    private val isResetPassword by lazy {
        args.phoneNumber.isNullOrBlank().not()
    }

    override fun onResume() {
        super.onResume()
        binding.isResetPassword = isResetPassword
        startTimer()
        initListener()
        observer()
    }

    private fun observer() {
        viewModel.mState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: VerificationActivityState) {
        when (state) {
            is VerificationActivityState.Init -> Unit
            is VerificationActivityState.SuccessAccountVerification -> handleSuccessAccountVerification(
                state.userEntity
            )
            is VerificationActivityState.ErrorAccountVerification -> handleErrorAccountVerification(
                state.wrappedResponse
            )
            is VerificationActivityState.IsLoading -> handleLoading(state.isLoading)
            is VerificationActivityState.Error -> onError(state.throwable)
            is VerificationActivityState.ErrorResetVerification -> onErrorResetVerification(state.response)
            is VerificationActivityState.SuccessResetVerification -> onSuccessResetVerification(
                state.createTokenResetPasswordEntity
            )
        }
    }

    private fun onSuccessResetVerification(createTokenResetPasswordEntity: CreateTokenResetPasswordEntity) {
        sharedPrefs.isNeedVerify(false)

        navigate(
            VerificationFragmentDirections.toCreatePasswordFragment(
                args.phoneNumber.orEmpty(),
                createTokenResetPasswordEntity.token
            )
        )
        viewModel.resetState()
    }

    private fun onErrorResetVerification(response: WrappedResponse<CreateTokenResetPasswordEntity>) {
        showErrorDialog(response.formattedErrors())
    }

    private fun onError(throwable: Throwable) {
        Log.e(APP_TAG, throwable.stackTraceToString())
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.btnSendOtp.isEnabled = !isLoading
        binding.loader.loadingProgressBar.start(isLoading)
    }

    private fun handleSuccessAccountVerification(userEntity: UserEntity) {
        sharedPrefs.saveToken(userEntity.token.orEmpty())
        findNavController().popBackStack(R.id.homeFragment, inclusive = true)
    }

    private fun handleErrorAccountVerification(response: WrappedResponse<UserResponse>) {
        showErrorDialog(response.formattedErrors())
    }

    private fun initListener() {
        binding.txtResend.setOnClickListener(this)
        binding.btnSendOtp.setOnClickListener(this)
        binding.otpView.doAfterTextChanged {
            binding.btnSendOtp.isEnabled = validate()
        }
    }

    private fun startTimer() {
        binding.txtResend.isEnabled = false
        object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val sec = millisUntilFinished / 1000
                binding.txtResend.text = getString(R.string.resend_code_after, sec)
            }

            override fun onFinish() {
                binding.txtResend.text = getString(R.string.resend_code_str)
                binding.txtResend.isEnabled = true
            }
        }.start()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.txtResend.id -> resendCode()
            binding.btnSendOtp.id -> submit()
        }
    }

    private fun resendCode() {
        binding.otpView.text?.clear()
        sendCode()
        startTimer()
    }

    private fun sendCode() {
        if (isResetPassword)
            viewModel.requestPasswordReset(args.phoneNumber.orEmpty())
        else
            viewModel.resendActivationCode()
    }

    private fun submit() {
        if (args.phoneNumber != null)
            createResetPasswordToken()
        else
            activeAccount()
    }

    private fun activeAccount() {
        viewModel.activeAccount(
            ActiveAccountRequest(
                binding.otpView.text.toString(),
                "0",
                "",
            )
        )
    }

    private fun createResetPasswordToken() {
        viewModel.createTokenResetPassword(
            args.phoneNumber.orEmpty(),
            binding.otpView.text.toString()
        )
    }

    private fun validate(): Boolean {
        if (binding.otpView.text!!.length < 4) // when enter code (4 digit)
            return false
        return true
    }
}