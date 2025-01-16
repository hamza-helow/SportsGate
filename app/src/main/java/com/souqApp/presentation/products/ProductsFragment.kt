package com.souqApp.presentation.products

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.NavGraphDirections
import com.souqApp.databinding.FragmentProductsBinding
import com.souqApp.presentation.base.BaseFragment
import com.souqApp.presentation.main.home.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProductsFragment : BaseFragment<FragmentProductsBinding>(FragmentProductsBinding::inflate) {

    private val viewModel: ProductsViewModel by viewModels()
    private val args: ProductsFragmentArgs by navArgs()

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfig

    private lateinit var productsAdapter: ProductGridPagingAdapter
    override fun onStart() {
        super.onStart()
        setupAdapter()
        observeToProducts()
    }

    private fun observeToProducts() {
        viewModel.productsLiveData.observe(viewLifecycleOwner) { result ->
            lifecycleScope.launch {
                productsAdapter.submitData(viewLifecycleOwner.lifecycle, result)
                productsAdapter.loadStateFlow.collect { loadStates ->
                    if (loadStates.refresh is LoadState.Loading) {
                        showLoading(true)
                    } else {
                        showLoading(false)
                        binding.showEmptyState = (productsAdapter.itemCount == 0)
                    }
                }
            }
        }
    }

    private fun setupAdapter() {
        productsAdapter =
            ProductGridPagingAdapter { navigate(NavGraphDirections.toProductDetailsFragment(it)) }
        binding.recProducts.addItemDecoration(SpacesItemDecoration(20))
        binding.recProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recProducts.adapter = productsAdapter
    }

    override fun updateTitleBar() = args.name
}