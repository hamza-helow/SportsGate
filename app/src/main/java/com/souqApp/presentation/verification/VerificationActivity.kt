package com.souqApp.presentation.verification

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.souqApp.databinding.ActivityVerificationBinding
import android.os.CountDownTimer
import android.view.View
import androidx.core.widget.doAfterTextChanged


class VerificationActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startTimer()
        initListener()

    }

    private fun initListener() {
        binding.txtResend.setOnClickListener(this)
        binding.otpView.doAfterTextChanged {
            binding.btnSendOtp.isEnabled = validate()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startTimer() {
        binding.txtResend.isEnabled = false
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.txtResend.text =
                    "Resend code after " + millisUntilFinished / 1000 + " sec"
            }

            override fun onFinish() {
                binding.txtResend.text = "Resend code"
                binding.txtResend.isEnabled = true
            }
        }.start()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.txtResend.id -> startTimer()
        }
    }

    private fun validate(): Boolean {
        if (binding.otpView.text!!.length < 4) // when enter code (4 digit)
            return false
        return true
    }
}