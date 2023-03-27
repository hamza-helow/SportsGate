package com.souqApp.presentation.main.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.common.CategoryEntity
import com.souqApp.databinding.FragmentCategoriesBinding
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.extension.showToast
import com.souqApp.presentation.base.BaseFragment
import com.souqApp.presentation.common.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CategoriesFragment :
    BaseFragment<FragmentCategoriesBinding>(FragmentCategoriesBinding::inflate) {

    private val viewModel: CategoriesViewModel by viewModels()
    private lateinit var progressBar: ProgressDialog

    private val adapterCategory by lazy {
        CategoryAdapter {
            if (it.children == null)
                navigate(CategoriesFragmentDirections.toSubCategoriesGraph(it.name ?: "", it.id))
            else
                navigate(CategoriesFragmentDirections.toCategoryChildrenFragment(it.children.toTypedArray()))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = ProgressDialog(requireContext())
        binding.recCategory.layoutManager = LinearLayoutManager(requireContext())
        binding.recCategory.adapter = adapterCategory
        observe()
    }

    private fun observe() {
        viewModel.mState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: CategoriesFragmentState) {
        when (state) {
            is CategoriesFragmentState.Init -> Unit
            is CategoriesFragmentState.CategoriesLoaded -> handleCategoriesLoaded(state.categories)
            is CategoriesFragmentState.IsLoading -> handleLoading(state.isLoading)
            is CategoriesFragmentState.ShowToast -> handleShowToast(state.message)
            is CategoriesFragmentState.CategoriesError -> handleErrorLoadCategories(state.response)
        }
    }

    private fun handleErrorLoadCategories(response: WrappedListResponse<CategoryEntity>) {
        progressBar.showLoader(false)
        requireContext().showGenericAlertDialog(response.formattedErrors())
    }

    private fun handleShowToast(message: String) {
        requireContext().showToast(message)
    }

    private fun handleLoading(loading: Boolean) {
        progressBar.showLoader(loading)
    }

    private fun handleCategoriesLoaded(categories: List<CategoryEntity>) {
        adapterCategory.list = categories
    }

    companion object {
        @JvmStatic
        fun newInstance() = CategoriesFragment()
    }
}