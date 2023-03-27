package com.souqApp.presentation.products_by_type

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.NavGraphDirections
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.products_by_type.remote.dto.ProductsByTypeRequest
import com.souqApp.data.products_by_type.remote.dto.ProductsByTypeResponse
import com.souqApp.databinding.FragmentProductsByTypeBinding
import com.souqApp.domain.products_by_type.ProductsByTypeEntity
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.presentation.base.BaseFragment
import com.souqApp.presentation.main.home.GridSpacingItemDecoration
import com.souqApp.presentation.main.home.ProductGridAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductsByTypeFragment :
    BaseFragment<FragmentProductsByTypeBinding>(FragmentProductsByTypeBinding::inflate) {

    @Inject
    lateinit var firebaseRemoteConfig: FirebaseRemoteConfig
    private val viewMode: ProductsByTypeVM by viewModels()
    private lateinit var productAdapter: ProductGridAdapter
    private val args: ProductsByTypeFragmentArgs by navArgs()

    override fun updateTitleBar(): String = args.subCategoryName

    override fun onResume() {
        super.onResume()
        viewMode.loadProducts(ProductsByTypeRequest(args.type, args.idSubCategory, 1))
        initRecycler()
        observer()
    }

    private fun initRecycler() {
        productAdapter = ProductGridAdapter(firebaseRemoteConfig) {
            navigate(NavGraphDirections.toProductDetailsFragment(it))
        }

        binding.recProducts.adapter = productAdapter
        binding.recProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recProducts.addItemDecoration(GridSpacingItemDecoration(2, 30, false))

    }

    private fun observer() {
        viewMode.mState.observe(this) {
            handleState(it)
        }
    }

    private fun handleState(state: ProductsByTypeActivityState) {

        when (state) {
            is ProductsByTypeActivityState.Loading -> handleLoading(state.isLoading)
            is ProductsByTypeActivityState.Init -> Unit
            is ProductsByTypeActivityState.ProductsLoaded -> handleProductsLoaded(state.productsByTypeEntity)
            is ProductsByTypeActivityState.ProductsErrorLoaded -> handleProductsLoadedError(state.productsByTypeResponse)
            is ProductsByTypeActivityState.OnError -> handleError(state.throwable)
        }

    }

    private fun handleError(throwable: Throwable) {
        Log.e(APP_TAG, throwable.stackTraceToString())
    }

    private fun handleProductsLoadedError(productsByTypeResponse: WrappedResponse<ProductsByTypeResponse>) {
        requireContext().showGenericAlertDialog(productsByTypeResponse.formattedErrors())
    }

    private fun handleProductsLoaded(productsByTypeEntity: ProductsByTypeEntity) {
        productAdapter.addList(productsByTypeEntity.products)
        binding.cardEmpty.isVisible = productAdapter.list.isEmpty()
        productAdapter.listenerNeedLoadMore = {
            viewMode.loadProducts(ProductsByTypeRequest(args.type, args.idSubCategory, it))
        }
    }

    private fun handleLoading(loading: Boolean) {
        binding.includeLoader.loadingProgressBar.start(loading && productAdapter.list.isEmpty())
    }
}