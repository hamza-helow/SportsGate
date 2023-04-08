package com.souqApp.presentation.products

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.FragmentProductsBinding
import com.souqApp.presentation.base.BaseFragment
import com.souqApp.presentation.main.home.ProductGridAdapter
import com.souqApp.presentation.main.home.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductsFragment : BaseFragment<FragmentProductsBinding>(FragmentProductsBinding::inflate) {

    private val viewModel: ProductsViewModel by viewModels()
    private val args: ProductsFragmentArgs by navArgs()

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfig

    private val productsAdapter: ProductGridAdapter by lazy {
        ProductGridAdapter(remoteConfig) {

        }
    }


    override fun onStart() {
        super.onStart()
        viewModel.loadProducts(args.categoryId)

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ProductsFragmentState.Loading -> showLoading(it.show)
                is ProductsFragmentState.OnProductsLoaded -> setupProductsAdapter(it.products)
            }
        }
    }

    private fun setupProductsAdapter(products: List<ProductEntity>) {
        binding.recProducts.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recProducts.addItemDecoration(SpacesItemDecoration(20))
        binding.recProducts.adapter = productsAdapter
        productsAdapter.list = products
    }

    override fun updateTitleBar() = args.name
}