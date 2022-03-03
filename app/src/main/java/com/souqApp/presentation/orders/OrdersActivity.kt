package com.souqApp.presentation.orders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.R
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.orders.remote.OrderResponse
import com.souqApp.databinding.ActivityOrdersBinding
import com.souqApp.domain.orders.OrderEntity
import com.souqApp.infra.extension.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrdersBinding
    private lateinit var ordersAdapter: OrdersAdapter

    private val viewModel: OrdersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ordersAdapter = OrdersAdapter()
        binding.recOrders.layoutManager = LinearLayoutManager(this)
        binding.recOrders.adapter = ordersAdapter

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setup(
            showTitleEnabled = true,
        )
        supportActionBar?.title = getString(R.string.my_orders_history_str)

        observer()
    }

    private fun observer() {

        viewModel.state.observe(this, { handleState(it) })
    }

    private fun handleState(state: OrdersActivityState?) {

        when (state) {
            is OrdersActivityState.Loading -> handleLoading(state.isLoading)
            is OrdersActivityState.Error -> handleError(state.throwable)
            is OrdersActivityState.OrdersLoaded -> handleOrdersLoaded(state.ordersEntity)
            is OrdersActivityState.OrdersErrorLoad -> handleOrdersErrorLoad(state.response)
        }
    }

    private fun handleOrdersErrorLoad(response: WrappedListResponse<OrderResponse>) {
        showGenericAlertDialog(response.formattedErrors())
    }

    private fun handleOrdersLoaded(ordersEntity: List<OrderEntity>) {
        ordersAdapter.list = ordersEntity
    }

    private fun handleError(throwable: Throwable) {
        if (throwable.message != null)
            showToast(throwable.message!!)

        Log.e("ERer" , throwable.stackTraceToString())
    }

    private fun handleLoading(loading: Boolean) {
        binding.recOrders.isVisible(!loading)
        binding.progressBar.start(loading)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}