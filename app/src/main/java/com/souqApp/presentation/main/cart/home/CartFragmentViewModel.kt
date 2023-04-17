package com.souqApp.presentation.main.cart.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.UpdateProductCartResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.GetCartDetailsUseCase
import com.souqApp.domain.main.cart.UpdateProductUseCase
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import com.souqApp.domain.main.cart.entity.ProductInCartEntity
import com.souqApp.domain.main.cart.entity.UpdateProductCartEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartFragmentViewModel @Inject constructor(

    private val getCartDetailsUseCase: GetCartDetailsUseCase,
    private val updateProductUseCase: UpdateProductUseCase) :
    ViewModel() {

    private val _state: MutableLiveData<CartFragmentState> = MutableLiveData(CartFragmentState.Init)
    val state: LiveData<CartFragmentState> get() = _state

    private fun setLoading(isLoading: Boolean) {
        _state.value = CartFragmentState.Loading(isLoading)
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

    private fun onUpdateProduct(updateProductQtyEntity: UpdateProductCartEntity) {
        _state.value = CartFragmentState.ProductUpdated(updateProductQtyEntity)
    }

    private fun onErrorUpdateProduct(response: WrappedResponse<UpdateProductCartResponse>) {
        _state.value = CartFragmentState.ErrorUpdateQuantity(response)
    }

    fun getCartDetails() {
        viewModelScope.launch {
            getCartDetailsUseCase.execute()
                .onStart {
                    setLoading(true)
                }
                .catch {

                    setLoading(false)
                    onError(it)
                }.collect {
                    setLoading(false)
                    when (it) {
                        is BaseResult.Success -> onCartDetailsLoaded(it.data)
                        is BaseResult.Errors -> onCartDetailsErrorLoaded(it.error)
                    }
                }
        }
    }

    fun updateProduct(product: ProductInCartEntity, isIncrease: Boolean) {
        viewModelScope.launch {
            updateProductUseCase.execute(product,isIncrease)
                .collect {
                    when (it) {
                        is BaseResult.Success -> onUpdateProduct(it.data)
                        is BaseResult.Errors -> onErrorUpdateProduct(it.error)
                    }
                }
        }
    }
}

sealed class CartFragmentState {
    object Init : CartFragmentState()
    data class Loading(val isLoading: Boolean) : CartFragmentState()

    data class ProductDelete(val deleted: Boolean) : CartFragmentState()

    data class CartDetailsLoaded(val cartDetailsEntity: CartDetailsEntity) : CartFragmentState()
    data class CartDetailsErrorLoaded(val wrappedResponse: WrappedResponse<CartDetailsResponse>) :
        CartFragmentState()

    data class ProductUpdated(val updateProductEntity: UpdateProductCartEntity) :
        CartFragmentState()

    data class ErrorUpdateQuantity(val response: WrappedResponse<UpdateProductCartResponse>) :
        CartFragmentState()

    data class Error(val throwable: Throwable) : CartFragmentState()

}