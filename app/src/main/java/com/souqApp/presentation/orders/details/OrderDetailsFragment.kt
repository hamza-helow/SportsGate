package com.souqApp.presentation.orders.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.orders.remote.dto.OrderDetailsResponse
import com.souqApp.databinding.FragmentOrderDetailsBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.orders.entity.OrderDetailsEntity
import com.souqApp.infra.extension.isVisible
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailsFragment :
    BaseFragment<FragmentOrderDetailsBinding>(FragmentOrderDetailsBinding::inflate) {

    private val args: OrderDetailsFragmentArgs by navArgs()
    private val viewModel: OrderDetailsViewModel by viewModels()
    private val productsOrderAdapter = ProductsOrderAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        observeToLoading()
        observeToOrderDetails()
    }

    private fun observeToOrderDetails() {
        viewModel.getOrderDetails(args.orderId)
        viewModel.orderDetailsLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is BaseResult.Errors -> handleErrorLoad(result.error)
                is BaseResult.Success -> handleLoaded(result.data)
            }
        }
    }

    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::handleLoading)
    }

    private fun initAdapter() {
        binding.recProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recProducts.adapter = productsOrderAdapter
    }

    private fun handleErrorLoad(response: WrappedResponse<OrderDetailsResponse>) {
        showDialog(response.message)
    }

    private fun handleLoaded(orderDetailsEntity: OrderDetailsEntity) {
        productsOrderAdapter.addList(orderDetailsEntity.products)
        binding.details = orderDetailsEntity
    }

    private fun handleLoading(loading: Boolean) {
        binding.content.isVisible(!loading)
        showLoading(loading)
    }
}