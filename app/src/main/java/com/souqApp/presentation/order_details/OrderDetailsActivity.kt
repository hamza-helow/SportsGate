package com.souqApp.presentation.order_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.R
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.order_details.remote.OrderDetailsEntity
import com.souqApp.databinding.ActivityOrderDetailsBinding
import com.souqApp.infra.extension.*
import com.souqApp.infra.utils.ID_ORDER
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailsBinding
    private val viewModel: OrderDetailsViewModel by viewModels()

    private val productsOrderAdapter = ProductsOrderAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setup(
            showTitleEnabled = true,
        )
        supportActionBar?.title = getString(R.string.order_details)

        binding.recProducts.layoutManager = LinearLayoutManager(this)
        binding.recProducts.adapter = productsOrderAdapter

        observer()
    }

    private fun observer() {
        viewModel.getOrderDetails(intent.getIntExtra(ID_ORDER, 0))
        viewModel.state.observe(this, { handleState(it) })
    }

    private fun handleState(state: OrderDetailsActivityState) {

        when (state) {
            is OrderDetailsActivityState.Loading -> handleLoading(state.isLoading)
            is OrderDetailsActivityState.Error -> handleError(state.throwable)
            is OrderDetailsActivityState.Loaded -> handleLoaded(state.orderDetailsEntity)
            is OrderDetailsActivityState.ErrorLoad -> handleErrorLoad(state.response)
        }
    }

    private fun handleErrorLoad(response: WrappedResponse<OrderDetailsEntity>) {
        showGenericAlertDialog(response.formattedErrors())
    }

    private fun handleLoaded(orderDetailsEntity: OrderDetailsEntity) {
        productsOrderAdapter.addList(orderDetailsEntity.products)
        binding.productInOrder = orderDetailsEntity
    }

    private fun handleError(throwable: Throwable) {
        if (throwable.message != null) {
            showToast(throwable.message!!)
        }
    }

    private fun handleLoading(loading: Boolean) {
        binding.content.isVisible(!loading)
        binding.progressBar.start(loading)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}