package com.souqApp.presentation.orders.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.orders.remote.OrderResponse
import com.souqApp.databinding.FragmentOrdersBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.orders.OrderEntity
import com.souqApp.infra.extension.isVisible
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment : BaseFragment<FragmentOrdersBinding>(FragmentOrdersBinding::inflate) {

    private lateinit var ordersAdapter: OrdersAdapter

    private val viewModel: OrdersViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        observeToOrders()
    }

    private fun initAdapter() {
        ordersAdapter = OrdersAdapter { navigate(OrdersFragmentDirections.toOrderDetailsFragment(it)) }
        binding.recOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.recOrders.adapter = ordersAdapter
    }

    private fun observeToOrders() {
        viewModel.orders.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                ordersAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                ordersAdapter.loadStateFlow.collect { loadStates ->
                    if (loadStates.refresh is LoadState.Loading) {
                        showLoading(true)
                    } else {
                        showLoading(false)
                    }
                }
            }
        }

    }


}