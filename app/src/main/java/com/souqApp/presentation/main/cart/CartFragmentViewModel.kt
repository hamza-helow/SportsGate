package com.souqApp.presentation.main.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.UpdateProductQtyResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.CartUseCase
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import com.souqApp.domain.main.cart.entity.UpdateProductQtyEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartFragmentViewModel @Inject constructor(private val cartUseCase: CartUseCase) :
    ViewModel() {

    private val _state: MutableLiveData<CartFragmentState> = MutableLiveData(CartFragmentState.Init)
    val state: LiveData<CartFragmentState> get() = _state

    private fun setLoading(isLoading: Boolean) {
        _state.value = CartFragmentState.Loading(isLoading)
    }

    private fun setUpdatingCart(updating: Boolean) {
        _state.value = CartFragmentState.UpdatingCart(updating)
    }

    private fun onCartDetailsLoaded(cartDetailsEntity: CartDetailsEntity) {
        _state.value = CartFragmentState.CartDetailsLoaded(cartDetailsEntity)
    }

    private fun onCartDetailsErrorLoaded(wrappedResponse: WrappedResponse<CartDetailsResponse>) {
        _state.value = CartFragmentState.CartDetailsErrorLoaded(wrappedResponse)
    }

    private fun onError(throwable: Throwable) {
        _state.value = CartFragmentState.Error(throwable)
    }

    private fun onUpdateQuantity(updateProductQtyEntity: UpdateProductQtyEntity) {
        _state.value = CartFragmentState.UpdateQuantity(updateProductQtyEntity)
    }

    private fun onErrorUpdateQuantity(response: WrappedResponse<UpdateProductQtyResponse>) {
        _state.value = CartFragmentState.ErrorUpdateQuantity(response)
    }

    private fun onDeleteProduct(deleted: Boolean) {
        _state.value = CartFragmentState.ProductDelete(deleted)
    }

    fun getCartDetails(isUpdate: Boolean = false) {
        viewModelScope.launch {
            cartUseCase.getCartDetails()
                .onStart {
                    if (isUpdate)
                        setUpdatingCart(true)
                    else
                        setLoading(true)
                }
                .catch {
                    if (isUpdate)
                        setUpdatingCart(false)
                    else
                        setLoading(false)
                    onError(it)
                }.collect {
                    if (isUpdate)
                        setUpdatingCart(false)
                    else
                        setLoading(false)

                    when (it) {
                        is BaseResult.Success -> onCartDetailsLoaded(it.data)
                        is BaseResult.Errors -> onCartDetailsErrorLoaded(it.error)
                    }
                }
        }
    }

    fun updateProductQty(productId: Int, qty: Int) {
        viewModelScope.launch {
            cartUseCase.updateProductQty(productId, qty)
                .onStart { setUpdatingCart(true) }
                .catch {
                    setUpdatingCart(false)
                    onError(it)
                }.collect {
                    setUpdatingCart(false)
                    when (it) {
                        is BaseResult.Success -> onUpdateQuantity(it.data)
                        is BaseResult.Errors -> onErrorUpdateQuantity(it.error)
                    }

                }
        }
    }

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            cartUseCase.deleteProductFromCart(productId)
                .onStart { setUpdatingCart(true) }
                .catch {
                    setUpdatingCart(false)
                    onError(it)
                }.collect {
                    setUpdatingCart(false)
                    onDeleteProduct(it)
                }
        }
    }

}

sealed class CartFragmentState {
    object Init : CartFragmentState()
    data class Loading(val isLoading: Boolean) : CartFragmentState()
    data class UpdatingCart(val updating: Boolean) : CartFragmentState()

    data class ProductDelete(val deleted: Boolean) : CartFragmentState()

    data class CartDetailsLoaded(val cartDetailsEntity: CartDetailsEntity) : CartFragmentState()
    data class CartDetailsErrorLoaded(val wrappedResponse: WrappedResponse<CartDetailsResponse>) :
        CartFragmentState()

    data class UpdateQuantity(val updateProductQtyEntity: UpdateProductQtyEntity) :
        CartFragmentState()

    data class ErrorUpdateQuantity(val response: WrappedResponse<UpdateProductQtyResponse>) :
        CartFragmentState()

    data class Error(val throwable: Throwable) : CartFragmentState()

}