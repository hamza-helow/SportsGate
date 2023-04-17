package com.souqApp.presentation.search

import android.util.Log
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.NavGraphDirections
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.FragmentSearchBinding
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate),
    SearchView.OnQueryTextListener, androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private val viewModel: SearchViewModel by viewModels()
    private val productHorizontalPagingAdapter by lazy {
        ProductHorizontalPagingAdapter {
            navigate(NavGraphDirections.toProductDetailsFragment(it))
        }
    }

    override fun showAppBar() = false

    override fun onResume() {
        super.onResume()
        binding.recProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recProducts.adapter = productHorizontalPagingAdapter
        binding.searchView.setOnQueryTextListener(this)
        observer()
    }

    private fun observer() {
        viewModel.search("")
        viewModel.state.observe(this) { handleState(it) }
    }

    private fun handleState(state: SearchActivityState) {
        when (state) {
            is SearchActivityState.Error -> handleError(state.throwable)
            is SearchActivityState.ErrorLoad -> handleErrorLoad(state.response)
            is SearchActivityState.Loaded -> handleLoaded(state.searchEntity)
            is SearchActivityState.Loading -> Unit
        }
    }


    private fun handleLoaded(result: PagingData<ProductEntity>) {
        lifecycleScope.launch {
            productHorizontalPagingAdapter.submitData(result)
            productHorizontalPagingAdapter.loadStateFlow.collectLatest {
                binding.includeLoader.loadingProgressBar.start(it.refresh == LoadState.Loading)
            }
        }
    }

    private fun handleErrorLoad(response: WrappedListResponse<ProductEntity>) {
        showErrorDialog(response.message)

    }

    private fun handleError(throwable: Throwable) {
        Log.e(APP_TAG, throwable.stackTraceToString())
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(text: String): Boolean {
        viewModel.search(text)
        return true
    }
}