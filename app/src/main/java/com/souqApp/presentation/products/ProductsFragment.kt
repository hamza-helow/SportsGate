package com.souqApp.presentation.products

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.NavGraphDirections
import com.souqApp.data.main.home.remote.dto.ProductEntity
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
    lateinit var remoteConfig: FirebaseRemoteConfig //ToDo

    private val productsAdapter: ProductGridPagingAdapter by lazy {
        ProductGridPagingAdapter {
            navigate(NavGraphDirections.toProductDetailsFragment(it))
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadProducts(args.categoryId , args.isPromo)

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ProductsFragmentState.Loading -> showLoading(it.show)
                is ProductsFragmentState.OnProductsLoaded -> setupProductsAdapter(it.result)
            }
        }
    }

    private fun setupProductsAdapter(result: PagingData<ProductEntity>) {
        binding.recProducts.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recProducts.addItemDecoration(SpacesItemDecoration(20))
        binding.recProducts.adapter = productsAdapter

        lifecycleScope.launch{
            productsAdapter.submitData(result)
        }
    }

    override fun updateTitleBar() = args.name
}