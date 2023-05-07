package com.souqApp.presentation.main.more.contact_us

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.souqApp.data.contact_us.remote.ContactUsRequest
import com.souqApp.databinding.FragmentContactUsBinding
import com.souqApp.infra.extension.*
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.net.SocketTimeoutException

@AndroidEntryPoint
class ContactUsFragment : BaseFragment<FragmentContactUsBinding>(FragmentContactUsBinding::inflate),
    View.OnClickListener {

    private val viewModel: ContactUsViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        observer()
        initListener()
    }


    private fun initListener() {
        binding.txtName.doAfterTextChanged { validate() }
        binding.txtEmail.doAfterTextChanged { validate() }
        binding.txtMessage.doAfterTextChanged { validate() }
        binding.includePhoneNumber.phoneEdt.doAfterTextChanged { validate() }
        binding.btnSubmit.setOnClickListener(this)
    }

    private fun observer() {
        viewModel.state.observe(this) { handleState(it) }
    }

    private fun handleState(state: ContactUsState) {

        when (state) {
            is ContactUsState.Added -> handleAdded(state.isAdded)
            is ContactUsState.Error -> handleError(state.throwable)
            is ContactUsState.Loading -> handleLoading(state.isLoading)
        }
    }

    private fun handleLoading(loading: Boolean) {
        binding.loader.loadingProgressBar.start(loading)
        binding.btnSubmit.isEnabled = !loading
    }

    private fun handleError(throwable: Throwable) {
        if (throwable is SocketTimeoutException) {
            requireContext().showToast("Unexpected error, try again later")
        }
    }

    private fun handleAdded(added: Boolean) {
        if (added) {
            requireContext().showToast("Your inquiry has been sent")
            findNavController().popBackStack()

        } else {
            requireContext().showToast("Unexpected error, try again later")
        }
    }


    private fun validate() {
        resetAllError()
        var isValid = true

        if (binding.txtName.text.isBlank()) {
            binding.txtName.errorBorder()
            isValid = false
        } else {
            binding.txtName.successBorder()
        }

        if (!binding.txtEmail.text.toString().isEmail()) {
            binding.txtEmail.errorBorder()
            isValid = false
        } else {
            binding.txtEmail.successBorder()
        }

        if (binding.txtMessage.text.isBlank()) {
            binding.txtMessage.errorBorder()
            isValid = false
        } else {
            binding.txtMessage.successBorder()
        }

        if (!binding.includePhoneNumber.phoneEdt.text.toString().toValidPhoneNumber().isPhone()) {
            binding.includePhoneNumber.root.errorBorder()
            isValid = false
        } else {
            binding.includePhoneNumber.root.successBorder()
        }

        binding.btnSubmit.isEnabled = isValid
    }

    private fun resetAllError() {
        binding.btnSubmit.isEnabled = false
        binding.txtName.noneBorder()
        binding.txtMessage.noneBorder()
        binding.txtEmail.noneBorder()
        binding.includePhoneNumber.root.noneBorder()
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.btnSubmit.id -> send()
        }
    }

    private fun send() {
        val name = binding.txtName.text.toString()
        val email = binding.txtEmail.text.toString()
        val message = binding.txtMessage.text.toString()
        val pone = binding.includePhoneNumber.phoneEdt.text.toString()

        viewModel.sendContactUsInfo(
            ContactUsRequest(
                name = name,
                email = email,
                phone = pone,
                message = message
            )
        )
    }
}