package com.souqApp.presentation.verification

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.souqApp.R
import com.souqApp.data.common.remote.dto.UserResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.verifcation.remote.dto.ActiveAccountRequest
import com.souqApp.data.verifcation.remote.dto.CreateTokenResetPasswordEntity
import com.souqApp.databinding.FragmentVerificationBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.base.BaseFragment
import com.souqApp.presentation.common.enums.VerificationType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VerificationFragment :
    BaseFragment<FragmentVerificationBinding>(FragmentVerificationBinding::inflate),
    View.OnClickListener {

    @Inject
    lateinit var sharedPrefs: SharedPrefs
    private val viewModel: VerificationViewModel by viewModels()
    private val args: VerificationFragmentArgs by navArgs()
    private val verificationType by lazy { args.verificationType }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeToLoading()
        startTimer()
        initListener()
    }

    private fun initUi() {

        binding.txtSubTitle.text = if (verificationType == VerificationType.ACTIVE_ACCOUNT) {
            getString(R.string.reset_pass_message)
        } else {
            if (verificationType == VerificationType.RESET_PASSWORD_BY_EMAIL)
                getString(R.string.verify_email)
            else
                getString(R.string.verify_phone_number)
        }

    }

    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::handleLoading)
    }


    private fun onSuccessResetVerification(createTokenResetPasswordEntity: CreateTokenResetPasswordEntity) {
//        navigate(
//            VerificationToResetPasswordFragmentDirections.toCreatePasswordFragment(
//                args.idCredential.orEmpty(),
//                createTokenResetPasswordEntity.token
//            )
//        ) //TODO
    }

    private fun onErrorResetVerification(response: WrappedResponse<CreateTokenResetPasswordEntity>) {
        showDialog(response.formattedErrors())
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.btnSendOtp.isEnabled = !isLoading
        binding.loader.loadingProgressBar.start(isLoading)
    }

    private fun handleSuccessAccountVerification(userEntity: UserEntity) {
        sharedPrefs.saveUserInfo(userEntity)
        sharedPrefs.saveToken(userEntity.token.orEmpty())
        findNavController().popBackStack(R.id.homeFragment, false)
    }

    private fun handleErrorAccountVerification(response: WrappedResponse<UserResponse>) {
        showDialog(response.formattedErrors())
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

        if (verificationType == VerificationType.ACTIVE_ACCOUNT) {
            viewModel.resendActivationCode()
        } else {
            viewModel.requestPasswordReset(
                credentialId = args.idCredential.orEmpty(),
                isPhone = verificationType == VerificationType.RESET_PASSWORD_BY_EMAIL
            )
        }


    }

    private fun submit() {
        if (verificationType == VerificationType.ACTIVE_ACCOUNT) {
            activeAccount()
        } else {
            createResetPasswordToken()
        }
    }

    private fun activeAccount() {
        viewModel.activeAccount(
            ActiveAccountRequest(
                binding.otpView.text.toString(),
                "0",
                "",
            )
        ) { result ->
            when (result) {
                is BaseResult.Errors -> handleErrorAccountVerification(result.error)
                is BaseResult.Success -> handleSuccessAccountVerification(result.data)
            }
        }
    }

    private fun createResetPasswordToken() {
        viewModel.createTokenResetPassword(
            args.idCredential.orEmpty(),
            binding.otpView.text.toString()
        ) { result ->
            when (result) {
                is BaseResult.Errors -> onErrorResetVerification(result.error)
                is BaseResult.Success -> onSuccessResetVerification(result.data)
            }
        }
    }

    private fun validate(): Boolean {
        if (binding.otpView.text!!.length < 4) // when enter code (4 digit)
            return false
        return true
    }
}