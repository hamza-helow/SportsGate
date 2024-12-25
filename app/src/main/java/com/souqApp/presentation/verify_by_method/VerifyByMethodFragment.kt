package com.souqApp.presentation.verify_by_method

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.souqApp.R
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.databinding.FragmentVerificationBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.base.BaseFragment
import com.souqApp.presentation.common.enums.VerificationType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VerifyByMethodFragment :
    BaseFragment<FragmentVerificationBinding>(FragmentVerificationBinding::inflate),
    View.OnClickListener {

    @Inject
    lateinit var sharedPrefs: SharedPrefs
    private val viewModel: VerifyByMethodViewModel by viewModels()
    private val args: VerifyByMethodFragmentArgs by navArgs()

    private val countDownTimer = object : CountDownTimer(60000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            val sec = millisUntilFinished / 1000
            binding.txtResend.text = getString(R.string.resend_code_after, sec)
        }

        override fun onFinish() {
            binding.txtResend.text = getString(R.string.resend_code_str)
            binding.txtResend.isEnabled = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeToLoading()
        startTimer()
        initListener()
    }

    private fun initUi() {
        binding.txtTitle.text = if (args.verifyType == VerificationType.BY_EMAIL)
            getString(R.string.verify_email)
        else
            getString(R.string.verify_phone_number)

    }

    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::handleLoading)
    }

    private fun handleLoading(isLoading: Boolean) {
        showLoading(isLoading)
    }

    private fun validate(): Boolean {
        return binding.otpView.text!!.length >= 4
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
        countDownTimer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
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


    private fun submit() {

        viewModel.verify(binding.otpView.text.toString()) { result ->
            when (result) {
                is BaseResult.Errors -> onErrorVerified(result.error)
                is BaseResult.Success -> onSuccessVerified(result.data)
            }
        }
    }

    private fun onSuccessVerified(userEntity: UserEntity) {
        sharedPrefs.saveUserInfo(userEntity)
        setFragmentResult(RESULT, bundleOf())
        findNavController().popBackStack(args.popupTo, false)
    }

    private fun onErrorVerified(response: WrappedResponse<UserResponse>) {
        showDialog(message = response.message)
    }

    private fun sendCode() {

    }


    companion object {
        const val RESULT = "verify_by_method_result"
    }
}