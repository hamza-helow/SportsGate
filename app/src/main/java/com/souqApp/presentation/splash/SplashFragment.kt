package com.souqApp.presentation.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.souqApp.R
import com.souqApp.data.common.utlis.CacheHelper
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.settings.remote.dto.PageEntity
import com.souqApp.databinding.FragmentSplashBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels()

    override fun showAppBar(): Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToPages()
    }

    private fun observeToPages() {
        viewModel.pagesLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is BaseResult.Errors -> onErrorLoad(result.error)
                is BaseResult.Success -> onLoaded(result.data)
            }
        }
    }

    private fun onLoaded(pages: List<PageEntity>) {
        CacheHelper.pages = pages

        navigate(SplashFragmentDirections.toHomeGraph(), R.id.splashFragment, inclusive = true)
    }

    private fun onErrorLoad(response: WrappedListResponse<PageEntity>) {
        showDialog(response.message, onConfirm = { viewModel.getPages() })
    }

}