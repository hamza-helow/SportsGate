package com.souqApp.presentation.splash

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.souqApp.data.common.utlis.CacheHelper
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.PageEntity
import com.souqApp.data.settings.remote.dto.SettingsEntity
import com.souqApp.databinding.FragmentSplashBinding
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels()

    override fun showAppBar(): Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
    }

    private fun observer() {
        viewModel.state.observe(viewLifecycleOwner) { handleState(it) }
    }

    private fun handleState(state: SplashFragmentState) {
        when (state) {
            is SplashFragmentState.Error -> Unit
            is SplashFragmentState.ErrorLoad -> onErrorLoad(state.response)
            is SplashFragmentState.Loaded -> onLoaded(state.pages)
        }
    }

    private fun onLoaded(pages: List<PageEntity>) {
        CacheHelper.pages = pages
        navigate(SplashFragmentDirections.toHomeFragment())
    }

    private fun onErrorLoad(response: WrappedListResponse<PageEntity>) {
        showDialog(response.message, onConfirm = { viewModel.getPages() })
    }

    private fun onError(throwable: Throwable) {
        Log.e(APP_TAG, throwable.stackTraceToString())
    }


}