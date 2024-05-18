package com.souqApp.presentation.main.more.page_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.souqApp.R
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.PageDetailsEntity
import com.souqApp.data.settings.remote.dto.SettingsEntity
import com.souqApp.databinding.FragmentPageDetailsBinding
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.extension.setContent
import com.souqApp.presentation.base.BaseFragment
import com.souqApp.presentation.main.more.home.MoreFragmentState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PageDetailsFragment :
    BaseFragment<FragmentPageDetailsBinding>(FragmentPageDetailsBinding::inflate) {

    private val args: PageDetailsFragmentArgs by navArgs()
    private val viewModel: PageDetailsViewModel by viewModels()

    override fun updateTitleBar(): String = args.page.title.orEmpty()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
    }

    private fun observer() {
        viewModel.getPageDetails(args.page.id)
        viewModel.state.observe(viewLifecycleOwner) { handleState(it) }
    }

    private fun handleState(state: PageDetailsState) {
        when (state) {
            is PageDetailsState.Error -> onError()
            is PageDetailsState.ErrorLoad -> onErrorLoad(state.response)
            is PageDetailsState.Loaded -> onLoaded(state.details)
            is PageDetailsState.Loading -> showLoading(true)
        }
    }

    private fun onLoaded(pageDetailsEntity: PageDetailsEntity) {
        binding.webView.setContent(pageDetailsEntity.content)
    }

    private fun onErrorLoad(response: WrappedResponse<PageDetailsEntity>) {
        showDialog(response.message)
    }

    private fun onError() {
        showDialog(getString(R.string.some_things_wrong))
    }

}