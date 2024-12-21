package com.souqApp.presentation.orders.home

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.databinding.FragmentOrdersBinding
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
        ordersAdapter = OrdersAdapter(onViewInvoice = ::viewInvoice) {
            navigate(OrdersFragmentDirections.toOrderDetailsFragment(it))
        }
        binding.recOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.recOrders.adapter = ordersAdapter
    }

    private fun viewInvoice(link: String) {
        val customTabsIntent: CustomTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(link))
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