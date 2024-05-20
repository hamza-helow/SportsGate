package com.souqApp.presentation.main.cart.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.R
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.UpdateProductCartResponse
import com.souqApp.databinding.FragmentCartBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import com.souqApp.domain.main.cart.entity.ProductInCartEntity
import com.souqApp.domain.main.cart.entity.UpdateProductCartEntity
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.extension.setupMenu
import com.souqApp.presentation.activity.MainViewModel
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate),
    View.OnClickListener {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: CartFragmentViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun hideBackButton(): Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCartAdapter()
        observeToCartDetails()
        observeToLoading()
        init()
    }

    private fun observeToLoading() {
        viewModel.loading.observe(viewLifecycleOwner, ::handleLoading)
    }

    private fun observeToCartDetails() {
        viewModel.cartDetailsLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is BaseResult.Errors -> handleCartDetailsErrorLoaded(result.error)
                is BaseResult.Success -> {
                    handleCartDetailsLoaded(result.data)
                }
            }
        }
    }

    private fun handleCartDetailsErrorLoaded(response: WrappedResponse<CartDetailsResponse>) {
        showDialog(response.message)
    }

    private fun handleCartDetailsLoaded(cartDetailsEntity: CartDetailsEntity) {
        val products = cartDetailsEntity.products
        handleCartEmptyState(products)
        binding.cart = cartDetailsEntity
        cartAdapter.addList(products)
        binding.recProducts.setAdapter(cartAdapter, LinearLayoutManager(requireContext()))
    }


    private fun init() {
        binding.btnCheckOut.setOnClickListener(this)
        binding.imgDeleteCart.setOnClickListener(this)
    }

    private fun resetCart() {
        viewModel.resetCart { result ->
            when (result) {
                is BaseResult.Errors -> Unit
                is BaseResult.Success -> {
                    cartAdapter.clearList()
                    mainViewModel.setQty(0)
                    handleCartEmptyState(cartAdapter.dataList)
                }
            }
        }
    }


    private fun handleUpdateQuantity(updateProductQtyEntity: UpdateProductCartEntity) {
        val product =
            cartAdapter.dataList.firstOrNull { it.cartItemId == updateProductQtyEntity.cartItemId }
        val index = cartAdapter.dataList.indexOf(product)
        val updatedQty = updateProductQtyEntity.productQty
        mainViewModel.setQty(updatedQty)
        if (updatedQty == 0) {
            cartAdapter.removeItem(index)
            handleCartEmptyState(cartAdapter.dataList)
        } else {
            product?.qty = updatedQty
            product?.totalPrice = updateProductQtyEntity.itemsPrice
            binding.cart?.subTotal = updateProductQtyEntity.subTotal
            binding.cart?.isAbleToPlaceOrder = updateProductQtyEntity.isAbleToPlaceOrder
            binding.cart?.placeOrderPercentage = updateProductQtyEntity.placeOrderPercentage
            binding.cart?.placeOrderAmount = updateProductQtyEntity.placeOrderAmount
            binding.invalidateAll()
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

    private fun handleCartEmptyState(products: List<ProductInCartEntity>) {
        binding.recProducts.setupEmptyState(products.isEmpty())
        binding.cardCheckOut.isVisible = products.isNotEmpty()
    }

    private fun initCartAdapter() {
        cartAdapter = CartAdapter { product, isIncrease ->
            viewModel.updateProduct(product, isIncrease) {
                when (it) {
                    is BaseResult.Errors -> handleErrorUpdateQuantity(it.error)
                    is BaseResult.Success -> handleUpdateQuantity(it.data)
                }
            }
        }
    }


    override fun onClick(view: View) {
        when (view.id) {
            binding.btnCheckOut.id -> navigateToPaymentDetails()
            binding.imgDeleteCart.id -> resetCart()
        }
    }

    private fun navigateToPaymentDetails() {
        navigate(CartFragmentDirections.toPaymentDetailsFragment())
    }
}