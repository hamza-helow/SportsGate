package com.souqApp.presentation.verification

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
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.common.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class VerificationActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var sharedPrefs: SharedPrefs
    private val tag: String = VerificationActivity::class.java.simpleName
    private lateinit var binding: ActivityVerificationBinding
    private val viewModel: VerificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            is VerificationActivityState.SuccessVerification -> handleSuccessVerification(state.userEntity)
            is VerificationActivityState.ErrorVerification -> handleErrorVerification(state.wrappedResponse)
            is VerificationActivityState.IsLoading -> handleLoading(state.isLoading)
            is VerificationActivityState.ShowToast -> handleToast(state.message)
        }
    }

    private fun handleToast(message: String) {
        showToast(message)
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.btnSendOtp.isEnabled = !isLoading
        binding.loader.loadingProgressBar.start(isLoading)
    }

    private fun handleSuccessVerification(userEntity: UserEntity) {
        sharedPrefs.saveToken(userEntity.token)
    }

    private fun handleErrorVerification(wrappedResponse: WrappedResponse<UserResponse>) {
        Log.e(tag, wrappedResponse.errors.toString())
    }

    private fun initListener() {
        binding.txtResend.setOnClickListener(this)
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

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.txtResend.id -> startTimer()
            binding.btnSendOtp.id -> activeAccount()
        }
    }

    private fun activeAccount() {
        viewModel.activeAccount(
            ActiveAccountRequest(
                binding.otpView.text!!.toString(),
                "0",
                "",
                "en"
            )
        )
    }

    private fun validate(): Boolean {
        if (binding.otpView.text!!.length < 4) // when enter code (4 digit)
            return false
        return true
    }
}