package com.souqApp.presentation.products

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.NavGraphDirections
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.FragmentProductsBinding
import com.souqApp.infra.custome_view.flex_recycler_view.PaginationListener
import com.souqApp.presentation.base.BaseFragment
import com.souqApp.presentation.main.home.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductsFragment : BaseFragment<FragmentProductsBinding>(FragmentProductsBinding::inflate) {

    private val viewModel: ProductsViewModel by viewModels()
    private val args: ProductsFragmentArgs by navArgs()

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfig

    private lateinit var productsAdapter: ProductGridAdapter
    override fun onStart() {
        super.onStart()
        setupAdapter()
        observeToState()
    }

    private fun observeToState() {
        viewModel.loadProducts(args.categoryId, args.type)
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ProductsFragmentState.OnProductsLoaded -> handleOnProductsLoaded(it.result)
                is ProductsFragmentState.Loading -> showLoading(it.show)
            }
        }
    }

    private fun setupAdapter() {
        productsAdapter = ProductGridAdapter { navigate(NavGraphDirections.toProductDetailsFragment(it)) }
        productsAdapter.setPaginationListener(object : PaginationListener {
            override val startPage: Int get() = 1

            override val isLastPage: Boolean get() = viewModel.isLastPage

            override fun loadMore(pageNumber: Int) {
                viewModel.loadProducts(args.categoryId, args.type, pageNumber)
            }
        })
    }

    private fun handleOnProductsLoaded(products: List<ProductEntity>) {
        binding.showEmptyState = products.isEmpty()
        productsAdapter.addList(products)
        binding.recProducts.addItemDecoration(SpacesItemDecoration(20))
        binding.recProducts.setAdapter(
            productsAdapter,
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        )
    }

    override fun updateTitleBar() = args.name
}