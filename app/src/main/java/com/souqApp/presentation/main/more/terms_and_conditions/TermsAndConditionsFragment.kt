package com.souqApp.presentation.main.more.terms_and_conditions

import android.util.Log
import androidx.fragment.app.viewModels
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.ContentEntity
import com.souqApp.databinding.FragmentTermsAndConditionsBinding
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsAndConditionsFragment :
    BaseFragment<FragmentTermsAndConditionsBinding>(FragmentTermsAndConditionsBinding::inflate) {

    private val viewModel: TermsAndConditionsViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        viewModel.state.observe(this) { handleState(it) }
    }

    private fun handleState(state: TermsAndConditionsActivityState) {

        when (state) {
            is TermsAndConditionsActivityState.Error -> onError(state.throwable)
            is TermsAndConditionsActivityState.ErrorLoad -> onErrorLoad(state.response)
            is TermsAndConditionsActivityState.Loaded -> onLoaded(state.contentEntity)
            is TermsAndConditionsActivityState.Loading -> onLoading(state.isLoading)

        }
    }

    private fun onLoading(loading: Boolean) {
        binding.progressBar.start(loading)
    }

    private fun onLoaded(contentEntity: ContentEntity) {
        binding.content = contentEntity.content
    }

    private fun onErrorLoad(response: WrappedResponse<ContentEntity>) {
        showDialog(response.message)
    }

    private fun onError(throwable: Throwable) {
        Log.e(APP_TAG, throwable.stackTraceToString())
    }

}