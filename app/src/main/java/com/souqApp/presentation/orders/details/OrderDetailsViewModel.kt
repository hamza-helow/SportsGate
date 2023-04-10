package com.souqApp.presentation.orders.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.orders.remote.OrderDetailsResponse
import com.souqApp.domain.orders.OrderDetailsEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.orders.OrderDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(private val orderDetailsUseCase: OrderDetailsUseCase) :
    ViewModel() {

    private val _state = MutableLiveData<OrderDetailsActivityState>()
    val state: LiveData<OrderDetailsActivityState> get() = _state

    private fun setLoading(isLoading: Boolean) {
        _state.value = OrderDetailsActivityState.Loading(isLoading)
    }

    private fun onError(throwable: Throwable) {
        _state.value = OrderDetailsActivityState.Error(throwable)
    }

    private fun onLoaded(orderDetailsEntity: OrderDetailsEntity) {
        _state.value = OrderDetailsActivityState.Loaded(orderDetailsEntity)
    }

    private fun onErrorLoad(response: WrappedResponse<OrderDetailsResponse>) {
        _state.value = OrderDetailsActivityState.ErrorLoad(response)
    }

    fun getOrderDetails(orderId: Int) {
        viewModelScope.launch {

            orderDetailsUseCase
                .getOrderDetails(orderId)
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    when (it) {
                        is BaseResult.Success -> onLoaded(it.data)
                        is BaseResult.Errors -> onErrorLoad(it.error)
                    }
                }
        }
    }
}


sealed class OrderDetailsActivityState {

    data class Loading(val isLoading: Boolean) : OrderDetailsActivityState()
    data class Error(val throwable: Throwable) : OrderDetailsActivityState()
    data class Loaded(val orderDetailsEntity: OrderDetailsEntity) : OrderDetailsActivityState()
    data class ErrorLoad(val response: WrappedResponse<OrderDetailsResponse>) :
        OrderDetailsActivityState()
}