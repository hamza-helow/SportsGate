package com.souqApp.presentation.search

import android.util.Log
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.NavGraphDirections
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.search.remote.SearchEntity
import com.souqApp.databinding.FragmentSearchBinding
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.presentation.base.BaseFragment
import com.souqApp.presentation.common.ProductHorizontalAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate), SearchView.OnQueryTextListener {

    lateinit var filterBottomSheet: FilterBottomSheet
    private val viewModel: SearchViewModel by viewModels()
    private val searchAdapter by lazy {
        ProductHorizontalAdapter{
            navigate(NavGraphDirections.toProductDetailsFragment(it))
        }
    }

    override fun showAppBar() = false

    override fun onResume() {
        super.onResume()

        binding.recProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recProducts.adapter = searchAdapter
        binding.searchView.setOnQueryTextListener(this)

        filterBottomSheet = FilterBottomSheet()

        filterBottomSheet.onSelectItem = {
            searchAdapter.clearList()
            viewModel.search(binding.searchView.query.toString(), 1, it.type)
        }
        observer()

        searchAdapter.listenerNeedLoadMore = {
            viewModel.search(binding.searchView.query.toString(), it)
        }

        binding.imgSort.setOnClickListener {
            filterBottomSheet.show(requireActivity().supportFragmentManager, "")
        }

    }

    private fun observer() {
        viewModel.search("", 1)
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


    private fun handleLoaded(searchEntity: SearchEntity) {
        searchAdapter.addList(searchEntity.products)
    }

    private fun handleErrorLoad(response: WrappedResponse<SearchEntity>) {
        requireContext().showGenericAlertDialog(response.formattedErrors())
    }

    private fun handleError(throwable: Throwable) {
        Log.e(APP_TAG, throwable.stackTraceToString())
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(text: String): Boolean {
        searchAdapter.clearList()
        viewModel.search(text, 1)
        return true
    }
}