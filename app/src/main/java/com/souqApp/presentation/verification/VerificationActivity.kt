package com.souqApp.presentation.verification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.souqApp.databinding.ActivityVerificationBinding
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.souqApp.R
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.dto.ActiveAccountRequest
import com.souqApp.data.verifcation.remote.dto.CreateTokenResetPasswordEntity
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.infra.utils.PHONE_NUMBER
import com.souqApp.infra.utils.RESET_TOKEN
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.create_password.CreatePasswordActivity
import com.souqApp.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class VerificationActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var sharedPrefs: SharedPrefs
    private lateinit var binding: ActivityVerificationBinding
    private val viewModel: VerificationViewModel by viewModels()

    private var phoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        phoneNumber = intent.getStringExtra(PHONE_NUMBER)
        binding.isResetPassword = phoneNumber != null

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

        val intent = Intent(this, CreatePasswordActivity::class.java)
        intent.putExtra(PHONE_NUMBER, phoneNumber)
        intent.putExtra(RESET_TOKEN, createTokenResetPasswordEntity.token)
        startActivity(intent)
    }

    private fun onErrorResetVerification(response: WrappedResponse<CreateTokenResetPasswordEntity>) {
        showGenericAlertDialog(response.formattedErrors())
    }


    private fun onError(throwable: Throwable) {
        Log.e(APP_TAG, throwable.stackTraceToString())
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.btnSendOtp.isEnabled = !isLoading
        binding.loader.loadingProgressBar.start(isLoading)
    }

    private fun handleSuccessAccountVerification(userEntity: UserEntity) {
        sharedPrefs.saveToken(userEntity.token ?: "")
        navigateToMainActivity()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //clear back stack
        startActivity(intent)
    }

    private fun handleErrorAccountVerification(wrappedResponse: WrappedResponse<UserResponse>) {
        showGenericAlertDialog(wrappedResponse.formattedErrors())
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
            binding.txtResend.id -> startTimer()
            binding.btnSendOtp.id -> submit()
        }
    }

    private fun submit() {
        if (phoneNumber != null)
            createResetPasswordToken()
        else
            activeAccount()
    }

    private fun activeAccount() {
        viewModel.activeAccount(
            ActiveAccountRequest(
                binding.otpView.text!!.toString(),
                "0",
                "",
            )
        )
    }

    private fun createResetPasswordToken() {
        viewModel.createTokenResetPassword(phoneNumber!!, binding.otpView.text!!.toString())
    }

    private fun validate(): Boolean {
        if (binding.otpView.text!!.length < 4) // when enter code (4 digit)
            return false
        return true
    }

}