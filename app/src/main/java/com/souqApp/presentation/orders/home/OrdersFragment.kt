package com.souqApp.presentation.orders.home

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.orders.remote.OrderResponse
import com.souqApp.databinding.FragmentOrdersBinding
import com.souqApp.domain.orders.OrderEntity
import com.souqApp.infra.extension.isVisible
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : BaseFragment<FragmentOrdersBinding>(FragmentOrdersBinding::inflate) {

    private lateinit var ordersAdapter: OrdersAdapter

    private val viewModel: OrdersViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        ordersAdapter = OrdersAdapter {
            navigate(OrdersFragmentDirections.toOrderDetailsFragment(it))
        }
        binding.recOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.recOrders.adapter = ordersAdapter
        observer()
    }

    private fun observer() {

        viewModel.state.observe(this) { handleState(it) }
    }

    private fun handleState(state: OrdersActivityState) {

        when (state) {
            is OrdersActivityState.Loading -> handleLoading(state.isLoading)
            is OrdersActivityState.Error -> handleError(state.throwable)
            is OrdersActivityState.OrdersLoaded -> handleOrdersLoaded(state.ordersEntity)
            is OrdersActivityState.OrdersErrorLoad -> handleOrdersErrorLoad(state.response)
        }
    }

    private fun handleOrdersErrorLoad(response: WrappedListResponse<OrderResponse>) {
        showDialog(response.message)
    }

    private fun handleOrdersLoaded(ordersEntity: List<OrderEntity>) {
        ordersAdapter.list = ordersEntity
    }

    private fun handleError(throwable: Throwable) {
        Log.e("TAG" , throwable.stackTraceToString())
    }

    private fun handleLoading(loading: Boolean) {
        binding.recOrders.isVisible(!loading)
        showLoading(loading)
    }

}