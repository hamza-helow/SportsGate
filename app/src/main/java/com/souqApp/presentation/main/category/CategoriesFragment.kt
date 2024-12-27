package com.souqApp.presentation.main.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.souqApp.NavGraphDirections
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.common.CategoryEntity
import com.souqApp.databinding.FragmentCategoriesBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.entity.ProductsType
import com.souqApp.infra.extension.showToast
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment :
    BaseFragment<FragmentCategoriesBinding>(FragmentCategoriesBinding::inflate),
    SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: CategoriesViewModel by viewModels()

    override fun hideBackButton(): Boolean = true

    private val adapterCategory by lazy {
        CategoryAdapter {
            if (it.children == null)
                navigate(
                    NavGraphDirections.toProductsFragment(
                        it.name.orEmpty(),
                        it.id,
                        ProductsType.CATEGORY
                    )
                )
            else
                navigate(
                    CategoriesFragmentDirections.toCategoryChildrenFragment(
                        it.name.orEmpty(),
                        it.children.toTypedArray()
                    )
                )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToLoading()
        initAdapter()
        observeToCategories()
    }

    private fun observeToCategories() {
        viewModel.categoriesLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is BaseResult.Errors -> handleErrorLoadCategories(result.error)
                is BaseResult.Success -> handleCategoriesLoaded(result.data)
            }
        }
    }

    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::handleLoading)
    }

    private fun initAdapter() {
        binding.recCategory.layoutManager = LinearLayoutManager(requireContext())
        binding.recCategory.adapter = adapterCategory
        binding.refreshSwiper.setOnRefreshListener(this)
    }

    private fun handleErrorLoadCategories(response: WrappedListResponse<CategoryEntity>) {
        showLoading(false)
        showDialog(response.message)
    }

    private fun handleShowToast(message: String) {
        requireContext().showToast(message)
    }

    private fun handleLoading(loading: Boolean) {
        showLoading(loading)
    }

    private fun handleCategoriesLoaded(categories: List<CategoryEntity>) {
        adapterCategory.list = categories
    }

    override fun onRefresh() {
        viewModel.getCategories()
        binding.refreshSwiper.isRefreshing = false
    }

}