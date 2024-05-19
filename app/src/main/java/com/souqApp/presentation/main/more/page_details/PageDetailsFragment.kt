package com.souqApp.presentation.main.more.page_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.PageDetailsEntity
import com.souqApp.databinding.FragmentPageDetailsBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.infra.extension.setContent
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PageDetailsFragment :
    BaseFragment<FragmentPageDetailsBinding>(FragmentPageDetailsBinding::inflate) {

    private val args: PageDetailsFragmentArgs by navArgs()
    private val viewModel: PageDetailsViewModel by viewModels()

    override fun updateTitleBar(): String = args.page.title.orEmpty()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToLoading()
        observeToPageDetails()
    }

    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::showLoading)
    }

    private fun observeToPageDetails() {
        viewModel.getPageDetails(args.page.id)
        viewModel.pageDetailsLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is BaseResult.Errors -> onErrorLoad(result.error)
                is BaseResult.Success -> onLoaded(result.data)
            }
        }
    }

    private fun onLoaded(pageDetailsEntity: PageDetailsEntity) {
        binding.webView.setContent(pageDetailsEntity.content)
    }

    private fun onErrorLoad(response: WrappedResponse<PageDetailsEntity>) {
        showDialog(response.message)
    }
}