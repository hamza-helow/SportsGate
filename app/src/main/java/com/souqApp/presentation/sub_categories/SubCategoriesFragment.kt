package com.souqApp.presentation.sub_categories

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.souqApp.databinding.FragmentSubCategoriesBinding
import com.souqApp.domain.sub_categories.SubCategoryEntity
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.PRODUCT_BY_SUB_CATEGORY
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubCategoriesFragment :
    BaseFragment<FragmentSubCategoriesBinding>(FragmentSubCategoriesBinding::inflate),
    SwipeRefreshLayout.OnRefreshListener {

    private val args: SubCategoriesFragmentArgs by navArgs()
    private val viewModel: SubCategoriesViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        binding.refreshSwiper.setOnRefreshListener(this)
        viewModel.getSubCategories(categoryId = args.categoryId)
        observer()
    }

    private fun observer() {
        viewModel.mState.observe(this) { handleState(it) }
    }

    private fun handleState(state: SubCategoriesActivityState) {
        when (state) {
            is SubCategoriesActivityState.Init -> Unit
            is SubCategoriesActivityState.CatchError -> onCatchError(state.message)
            is SubCategoriesActivityState.ErrorLoading -> Unit
            is SubCategoriesActivityState.Loaded -> onLoaded(state.subCategories)
            is SubCategoriesActivityState.Loading -> onLoading(state.isLoading)
        }
    }

    private fun onLoading(loading: Boolean) {
        binding.includeLoader.loadingProgressBar.start(loading)
    }

    private fun onCatchError(message: String) {
    }

    private fun onLoaded(subCategories: List<SubCategoryEntity>) {
        val subCategoriesAdapter = SubCategoriesAdapter {
            navigate(
                SubCategoriesFragmentDirections.toProductsByTypeFragment(
                    PRODUCT_BY_SUB_CATEGORY,
                    it.id
                )
            )
        }
        subCategoriesAdapter.list = subCategories

        binding.recSubCategory.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recSubCategory.adapter = subCategoriesAdapter
    }

    override fun onRefresh() {
        viewModel.getSubCategories(categoryId = args.categoryId)
        binding.refreshSwiper.isRefreshing = false
    }
}