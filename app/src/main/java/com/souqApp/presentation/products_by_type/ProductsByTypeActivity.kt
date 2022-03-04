package com.souqApp.presentation.products_by_type

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.products_by_type.remote.dto.ProductsByTypeRequest
import com.souqApp.data.products_by_type.remote.dto.ProductsByTypeResponse
import com.souqApp.databinding.ActivityProductsByTypeBinding
import com.souqApp.domain.products_by_type.ProductsByTypeEntity
import com.souqApp.infra.extension.setup
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.infra.utils.ID_SUBCATEGORY
import com.souqApp.infra.utils.PRODUCTS_TYPE
import com.souqApp.presentation.main.home.ProductGridAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsByTypeActivity : AppCompatActivity() {

    private val viewMode: ProductsByTypeVM by viewModels()
    private lateinit var binding: ActivityProductsByTypeBinding
    private val productAdapter = ProductGridAdapter()

    private var type: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsByTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        type = intent.getIntExtra(PRODUCTS_TYPE, -1)
        val idSubCategory = intent.getIntExtra(ID_SUBCATEGORY, 0)

        viewMode.loadProducts(ProductsByTypeRequest(type, idSubCategory, 1))

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setup()

        binding.recProducts.layoutManager = GridLayoutManager(this, 2)
        binding.recProducts.adapter = productAdapter

        observer()
    }

    private fun observer() {

        viewMode.mState.observe(this, {
            handleState(it)
        })
    }

    private fun handleState(state: ProductsByTypeActivityState?) {

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
        showGenericAlertDialog(productsByTypeResponse.formattedErrors())
    }

    private fun handleProductsLoaded(productsByTypeEntity: ProductsByTypeEntity) {
        productAdapter.addList(productsByTypeEntity.products)
        binding.cardEmpty.isVisible = productAdapter.list.isEmpty()

        productAdapter.listenerNeedLoadMore = {
            viewMode.loadProducts(ProductsByTypeRequest(type, 0, it))
        }

    }

    private fun handleLoading(loading: Boolean) {
        binding.includeLoader.loadingProgressBar.start(loading && productAdapter.list.isEmpty())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}