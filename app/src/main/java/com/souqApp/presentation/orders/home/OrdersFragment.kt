package com.souqApp.presentation.orders.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.orders.remote.OrderResponse
import com.souqApp.databinding.FragmentOrdersBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.orders.OrderEntity
import com.souqApp.infra.extension.isVisible
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : BaseFragment<FragmentOrdersBinding>(FragmentOrdersBinding::inflate) {

    private lateinit var ordersAdapter: OrdersAdapter

    private val viewModel: OrdersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        observeToLoading()
        observeToOrders()

    }

    private fun observeToOrders() {
        viewModel.orderLiveData.observe(viewLifecycleOwner) { result ->
            when(result){
                is BaseResult.Errors -> handleOrdersErrorLoad(result.error)
                is BaseResult.Success -> handleOrdersLoaded(result.data)
            }
        }
    }

    private fun initAdapter() {
        ordersAdapter = OrdersAdapter {
            navigate(OrdersFragmentDirections.toOrderDetailsFragment(it))
        }
        binding.recOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.recOrders.adapter = ordersAdapter
    }

    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::handleLoading)
    }



    private fun handleOrdersErrorLoad(response: WrappedListResponse<OrderResponse>) {
        showDialog(response.message)
    }

    private fun handleOrdersLoaded(ordersEntity: List<OrderEntity>) {
        ordersAdapter.list = ordersEntity
    }

    private fun handleLoading(loading: Boolean) {
        binding.recOrders.isVisible(!loading)
        showLoading(loading)
    }

}