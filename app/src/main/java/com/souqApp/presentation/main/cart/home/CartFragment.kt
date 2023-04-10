package com.souqApp.presentation.main.cart.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.UpdateProductQtyResponse
import com.souqApp.databinding.FragmentCartBinding
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.extension.start
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.net.SocketTimeoutException

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate),
    View.OnClickListener {

    private val viewModel: CartFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    override fun onResume() {
        super.onResume()
        observer()
    }

    private fun init() {
        viewModel.getCartDetails()
        binding.btnCheckOut.setOnClickListener(this)
    }

    private fun observer() {
        viewModel.state.observe(viewLifecycleOwner) {
            handleState(it)
        }
    }

    private fun handleState(state: CartFragmentState) {
        when (state) {
            is CartFragmentState.Init -> Unit
            is CartFragmentState.Error -> handleError(state.throwable)
            is CartFragmentState.CartDetailsLoaded -> handleCartDetailsLoaded(state.cartDetailsEntity)
            is CartFragmentState.CartDetailsErrorLoaded -> handleCartDetailsErrorLoaded(state.wrappedResponse)
            is CartFragmentState.Loading -> handleLoading(state.isLoading)
            is CartFragmentState.UpdateQuantity -> reloadCart()
            is CartFragmentState.ProductDelete -> reloadCart()
            is CartFragmentState.UpdatingCart -> handleUpdatingCart(state.updating)
            is CartFragmentState.ErrorUpdateQuantity -> handleErrorUpdateQuantity(state.response)
        }
    }

    private fun handleErrorUpdateQuantity(response: WrappedResponse<UpdateProductQtyResponse>) {
        requireContext().showGenericAlertDialog(response.formattedErrors())
        reloadCart()
    }

    private fun handleUpdatingCart(updating: Boolean) {
        if (updating)
            binding.txtTotal.text = "..."
        else
            binding.txtTotal.text = "${binding.cart?.subTotal}"
        binding.btnCheckOut.isEnabled = !updating
    }

    private fun reloadCart() {
        viewModel.getCartDetails(isUpdate = true)
    }

    private fun handleLoading(loading: Boolean) {
        binding.progressBar.start(loading)
        binding.content.isVisible(!loading)
    }

    private fun handleCartDetailsErrorLoaded(wrappedResponse: WrappedResponse<CartDetailsResponse>) {
        requireContext().showGenericAlertDialog(wrappedResponse.formattedErrors())
    }

    private fun handleCartDetailsLoaded(cartDetailsEntity: CartDetailsEntity) {
        binding.content.isVisible(cartDetailsEntity.products.isNotEmpty())
        binding.cardCheckOut.isVisible(cartDetailsEntity.products.isNotEmpty())
        binding.cardCartEmpty.isVisible(cartDetailsEntity.products.isEmpty())
        binding.cart = cartDetailsEntity
        val cartAdapter = CartAdapter()
        cartAdapter.addList(cartDetailsEntity.products)
        cartAdapter.onChangeQTY = { viewModel.updateProductQty(it.id, it.qty) }

        binding.recProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recProducts.adapter = cartAdapter

    }

    private fun handleError(throwable: Throwable) {
        binding.content.isVisible(false)
        if (throwable is SocketTimeoutException) {
            requireContext().showToast("Unexpected error, try again later")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CartFragment()
    }

    override fun onClick(view: View) {

        when (view.id) {
            binding.btnCheckOut.id -> navigateToPaymentDetails()
        }

    }

    private fun navigateToPaymentDetails() {
        navigate(CartFragmentDirections.toPaymentDetailsFragment())
    }
}