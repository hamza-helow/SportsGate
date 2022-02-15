package com.souqApp.presentation.main.more.wish_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.databinding.ActivityWishListBinding
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.extension.start
import com.souqApp.presentation.common.ProductHorizontalAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.net.SocketTimeoutException

@AndroidEntryPoint
class WishListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWishListBinding
    private val viewModel: WishListViewModel by viewModels()
    private val adapter: ProductHorizontalAdapter = ProductHorizontalAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recProducts.layoutManager = LinearLayoutManager(this)
        binding.recProducts.adapter = adapter

        viewModel.state.observe(this, { handleState(it) })
    }

    private fun handleState(state: WishListActivityState?) {

        when (state) {
            is WishListActivityState.Error -> onError(state.throwable)
            is WishListActivityState.ErrorLoad -> onErrorLoad(state.response)
            is WishListActivityState.Loaded -> onLoaded(state.products)
            is WishListActivityState.Loading -> onLoading(state.isLoading)
        }
    }

    private fun onLoading(loading: Boolean) {
        binding.progressBar.start(loading)
        binding.recProducts.isVisible(!loading)
    }

    private fun onLoaded(products: List<ProductEntity>) {
        adapter.list = products
    }

    private fun onErrorLoad(response: WrappedListResponse<ProductEntity>) {
        showGenericAlertDialog(response.formattedErrors())
    }

    private fun onError(throwable: Throwable) {
        if (throwable is SocketTimeoutException) {
            showToast("Unexpected error, try again later")
        }
    }
}