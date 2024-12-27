package com.souqApp.presentation.forgot_password.reset_password_methods

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.databinding.FragmentResetPasswordMethodsBinding
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordMethodsFragment :
    BaseFragment<FragmentResetPasswordMethodsBinding>(FragmentResetPasswordMethodsBinding::inflate) {

    private val viewModel: ResetPasswordMethodsViewModel by viewModels()
    private lateinit var passwordMethodsAdapter: PasswordMethodsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToLoading()
        observeToMethods()
    }


    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::showLoading)
    }

    private fun observeToMethods() {
        viewModel.methodsLiveData.observe(viewLifecycleOwner, ::setMethodsAdapter)
    }

    private fun setMethodsAdapter(methods: List<String>) {
        passwordMethodsAdapter = PasswordMethodsAdapter(::handleClickItem)
        passwordMethodsAdapter.setList(methods)
        binding.recMethods.layoutManager = LinearLayoutManager(requireContext())
        binding.recMethods.adapter = passwordMethodsAdapter
    }

    private fun handleClickItem(method: String) {
       navigate( ResetPasswordMethodsFragmentDirections.toForgotPasswordFragment(method != EMAIL))
    }

    companion object {
        const val EMAIL = "email"
    }
}