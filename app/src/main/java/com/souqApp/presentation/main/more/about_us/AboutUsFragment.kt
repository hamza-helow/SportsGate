package com.souqApp.presentation.main.more.about_us

import android.util.Log
import androidx.fragment.app.viewModels
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.ContentEntity
import com.souqApp.databinding.FragmentAboutUsBinding
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AboutUsFragment : BaseFragment<FragmentAboutUsBinding>(FragmentAboutUsBinding::inflate) {

    private val viewModel: AboutUsViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        viewModel.state.observe(this) { handleState(it) }
    }

    private fun handleState(state: AboutUsActivityState) {
        when (state) {
            is AboutUsActivityState.Error -> onError(state.throwable)
            is AboutUsActivityState.ErrorLoad -> onErrorLoad(state.response)
            is AboutUsActivityState.Loaded -> onLoaded(state.contentEntity)
            is AboutUsActivityState.Loading -> onLoading(state.isLoading)
        }
    }

    private fun onLoading(loading: Boolean) {
        binding.progressBar.start(loading)
    }

    private fun onLoaded(contentEntity: ContentEntity) {
        binding.content = contentEntity.content
    }

    private fun onErrorLoad(response: WrappedResponse<ContentEntity>) {
        showErrorDialog(response.message)
    }

    private fun onError(throwable: Throwable) {
        Log.e(APP_TAG, throwable.stackTraceToString())
    }

}