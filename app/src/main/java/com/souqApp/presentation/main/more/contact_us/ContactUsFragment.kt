package com.souqApp.presentation.main.more.contact_us

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.souqApp.R
import com.souqApp.data.contact_us.remote.ContactUsRequest
import com.souqApp.databinding.FragmentContactUsBinding
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.extension.start
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactUsFragment : BaseFragment<FragmentContactUsBinding>(FragmentContactUsBinding::inflate),
    View.OnClickListener {

    private val viewModel: ContactUsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        observeToLoading()
        observeToValidate()
    }

    private fun observeToValidate() {
        validate()
        viewModel.validateLiveData.observe(viewLifecycleOwner) { enabled ->
            binding.btnSubmit.isEnabled = enabled
        }
    }

    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::handleLoading)
    }


    private fun initListener() {
        binding.txtName.doAfterTextChanged { validate() }
        binding.txtEmail.doAfterTextChanged { validate() }
        binding.txtMessage.doAfterTextChanged { validate() }
        binding.includePhoneNumber.phoneEdt.doAfterTextChanged { validate() }
        binding.btnSubmit.setOnClickListener(this)
    }

    private fun handleLoading(loading: Boolean) {
        binding.loader.loadingProgressBar.start(loading)
        binding.btnSubmit.isEnabled = !loading
    }

    private fun handleAdded(added: Boolean) {
        if (added) {
            requireContext().showToast(getString(R.string.your_inquiry_has_been_sent))
            findNavController().popBackStack(R.id.moreFragment, false)

        } else {
            requireContext().showToast(getString(R.string.unexpected_error_try_again_later))
        }
    }


    private fun validate() {

        viewModel.validate(
            name = binding.txtName.text.toString(),
            email = binding.txtEmail.text.toString(),
            message = binding.txtMessage.text.toString(),
            phoneNumber = binding.includePhoneNumber.phoneEdt.text.toString()
        )
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
        ) { handleAdded(it) }
    }
}