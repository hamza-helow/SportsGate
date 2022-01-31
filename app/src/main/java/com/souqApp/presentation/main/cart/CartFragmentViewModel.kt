package com.souqApp.presentation.main.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.CartUseCase
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
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

    private fun onCartDetailsLoaded(cartDetailsEntity: CartDetailsEntity) {
        _state.value = CartFragmentState.CartDetailsLoaded(cartDetailsEntity)
    }

    private fun onCartDetailsErrorLoaded(wrappedResponse: WrappedResponse<CartDetailsResponse>) {
        _state.value = CartFragmentState.CartDetailsErrorLoaded(wrappedResponse)
    }

    private fun onError(throwable: Throwable) {
        _state.value = CartFragmentState.Error(throwable)
    }


    @Inject
    fun getCartDetails() {
        viewModelScope.launch {
            cartUseCase.getCartDetails()
                .onStart { setLoading(true) }
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

}

sealed class CartFragmentState {
    object Init : CartFragmentState()
    data class Loading(val isLoading: Boolean) : CartFragmentState()
    data class CartDetailsLoaded(val cartDetailsEntity: CartDetailsEntity) : CartFragmentState()
    data class CartDetailsErrorLoaded(val wrappedResponse: WrappedResponse<CartDetailsResponse>) :
        CartFragmentState()

    data class Error(val throwable: Throwable) : CartFragmentState()

}