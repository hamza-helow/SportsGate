package com.souqApp.presentation.order_details

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.order_details.remote.OrderDetailsEntity
import com.souqApp.databinding.FragmentOrderDetailsBinding
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.extension.start
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailsFragment :
    BaseFragment<FragmentOrderDetailsBinding>(FragmentOrderDetailsBinding::inflate) {

    private val args: OrderDetailsFragmentArgs by navArgs()
    private val viewModel: OrderDetailsViewModel by viewModels()
    private val productsOrderAdapter = ProductsOrderAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.recProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recProducts.adapter = productsOrderAdapter
        observer()
    }

    private fun observer() {
        viewModel.getOrderDetails(args.orderId)
        viewModel.state.observe(this) { handleState(it) }
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
        requireContext().showGenericAlertDialog(response.formattedErrors())
    }

    private fun handleLoaded(orderDetailsEntity: OrderDetailsEntity) {
        productsOrderAdapter.addList(orderDetailsEntity.products)
        binding.productInOrder = orderDetailsEntity
    }

    private fun handleError(throwable: Throwable) {
        if (throwable.message != null) {
            requireContext().showToast(throwable.message!!)
        }
    }

    private fun handleLoading(loading: Boolean) {
        binding.content.isVisible(!loading)
        binding.progressBar.start(loading)
    }
}