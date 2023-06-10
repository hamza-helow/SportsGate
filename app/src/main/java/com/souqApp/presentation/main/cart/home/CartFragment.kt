package com.souqApp.presentation.main.cart.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.UpdateProductCartResponse
import com.souqApp.databinding.FragmentCartBinding
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import com.souqApp.domain.main.cart.entity.ProductInCartEntity
import com.souqApp.domain.main.cart.entity.UpdateProductCartEntity
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.extension.showToast
import com.souqApp.presentation.activity.MainViewModel
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.net.SocketTimeoutException

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate),
    View.OnClickListener {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: CartFragmentViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun hideBackButton(): Boolean = true

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
            is CartFragmentState.ProductUpdated -> handleUpdateQuantity(state.updateProductEntity)
            is CartFragmentState.ProductDelete -> Unit
            is CartFragmentState.ErrorUpdateQuantity -> handleErrorUpdateQuantity(state.response)
        }
    }

    private fun handleUpdateQuantity(updateProductQtyEntity: UpdateProductCartEntity) {
        val product = cartAdapter.dataList.firstOrNull { it.cartItemId == updateProductQtyEntity.cartItemId }
        val index = cartAdapter.dataList.indexOf(product)
        val updatedQty = updateProductQtyEntity.productQty
        mainViewModel.setQty(updatedQty)
        if (updatedQty == 0) {
            cartAdapter.removeItem(index)
            handleCartEmptyState(cartAdapter.dataList)
        } else {
            product?.qty = updatedQty
            product?.totalPrice = updateProductQtyEntity.itemsPrice
            binding.txtTotal.text = updateProductQtyEntity.subTotal
            cartAdapter.notifyItemChanged(index)
        }
    }

    private fun handleErrorUpdateQuantity(response: WrappedResponse<UpdateProductCartResponse>) {
        showDialog(response.message)
    }

    private fun handleLoading(loading: Boolean) {
        showLoading(loading)
        binding.content.isVisible(!loading)
    }

    private fun handleCartDetailsErrorLoaded(response: WrappedResponse<CartDetailsResponse>) {
        showDialog(response.message)
    }

    private fun handleCartEmptyState(products: List<ProductInCartEntity>) {
        binding.recProducts.setupEmptyState(products.isEmpty())
        binding.cardCheckOut.isVisible = products.isNotEmpty()
    }

    private fun handleCartDetailsLoaded(cartDetailsEntity: CartDetailsEntity) {
        handleCartEmptyState(cartDetailsEntity.products)
        binding.cart = cartDetailsEntity

        cartAdapter = CartAdapter { product, isIncrease ->
            viewModel.updateProduct(product, isIncrease)
        }

        cartAdapter.addList(cartDetailsEntity.products)
        binding.recProducts.setAdapter(cartAdapter, LinearLayoutManager(requireContext()))

    }

    private fun handleError(throwable: Throwable) {
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