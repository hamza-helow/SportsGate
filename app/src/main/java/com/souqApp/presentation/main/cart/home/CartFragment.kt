package com.souqApp.presentation.main.cart.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.souqApp.NavGraphDirections
import com.souqApp.R
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.UpdateProductCartResponse
import com.souqApp.databinding.FragmentCartBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import com.souqApp.domain.main.cart.entity.ProductInCartEntity
import com.souqApp.domain.main.cart.entity.UpdateProductCartEntity
import com.souqApp.infra.custome_view.flex_recycler_view.showEmptyState
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.utils.SwipeToDeleteCallback
import com.souqApp.presentation.activity.MainViewModel
import com.souqApp.presentation.base.BaseFragment
import com.souqApp.presentation.common.enums.VerificationType
import com.souqApp.presentation.verify_by_method.VerifyByMethodFragment
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
        init()
        observeToVerifyByMethodFragmentResult()
        initCartAdapter()
        observeToCartDetails()
        observeToLoading()

    }

    private fun init() {
        binding.recProducts.setupEmptyState(viewModel.sharedPrefs.isLogin().not())
        binding.btnCheckOut.setOnClickListener(this)
        binding.imgDeleteCart.setOnClickListener(this)
        binding.layoutPhoneNotVerified.setOnClickListener(this)
        binding.layoutEmailNotVerified.setOnClickListener(this)
    }

    private fun observeToVerifyByMethodFragmentResult() {
        setFragmentResultListener(VerifyByMethodFragment.RESULT) { _, _ ->
            viewModel.getCartDetails()
        }
    }

    private fun observeToLoading() {
        viewModel.loadingCart.observe(viewLifecycleOwner, ::handleLoadingCart)
        viewModel.loadingVerifyMethod.observe(viewLifecycleOwner, ::handleLoadingVerifyMethod)
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

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                products.getOrNull(position)?.let(::deleteProduct)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recProducts.recyclerView)

        handleCartEmptyState(products)
        binding.cart = cartDetailsEntity
        cartAdapter.addList(products)
        binding.recProducts.setAdapter(cartAdapter, LinearLayoutManager(requireContext()))
    }


    fun deleteProduct(product: ProductInCartEntity) {
        viewModel.deleteProduct(product) {
            when (it) {
                is BaseResult.Errors -> handleErrorUpdateQuantity(it.error)
                is BaseResult.Success -> handleUpdateQuantity(it.data)
            }
        }
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

    private fun handleLoadingCart(loading: Boolean) {
        showLoading(loading)
        binding.content.isVisible(!loading)
    }

    private fun handleLoadingVerifyMethod(loading: Boolean) {
        showLoading(loading)
    }

    private fun handleCartEmptyState(products: List<ProductInCartEntity>) {
        val isEmptyState = products.isEmpty()
        binding.isEmptyState = isEmptyState
        binding.recProducts.setupEmptyState(isEmptyState)
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
            binding.layoutPhoneNotVerified.id -> verifyMethod(VerificationType.BY_PHONE)
            binding.layoutEmailNotVerified.id -> verifyMethod(VerificationType.BY_EMAIL)
        }
    }

    private fun verifyMethod(verificationType: VerificationType) {
        viewModel.sendOtpToVerifyMethod(verificationType) { result ->
            when (result) {
                is BaseResult.Errors -> showDialog(result.error.message)
                is BaseResult.Success -> navigate(
                    NavGraphDirections.toVerifyByMethodFragment(verificationType, R.id.cartFragment)
                )
            }
        }
    }

    private fun navigateToPaymentDetails() {
        navigate(CartFragmentDirections.toPaymentDetailsFragment())
    }
}