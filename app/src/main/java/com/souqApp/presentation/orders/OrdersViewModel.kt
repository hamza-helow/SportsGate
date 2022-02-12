package com.souqApp.presentation.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.orders.remote.OrderResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.orders.OrderEntity
import com.souqApp.domain.orders.OrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(private val ordersUseCase: OrdersUseCase) : ViewModel() {

    private val _state = MutableLiveData<OrdersActivityState>()
    val state: LiveData<OrdersActivityState> get() = _state


    private fun setLoading(isLoading: Boolean) {
        _state.value = OrdersActivityState.Loading(isLoading)
    }

    private fun onError(throwable: Throwable) {
        _state.value = OrdersActivityState.Error(throwable)
    }

    private fun onOrdersLoaded(ordersEntity: List<OrderEntity>) {
        _state.value = OrdersActivityState.OrdersLoaded(ordersEntity)
    }

    private fun onOrdersErrorLoad(response: WrappedListResponse<OrderResponse>) {
        _state.value = OrdersActivityState.OrdersErrorLoad(response)
    }

    @Inject
    fun getOrders() {
        viewModelScope.launch {

            ordersUseCase
                .getOrders()
                .onStart { setLoading(true) }
                .catch {
                    setLoading(false)
                    onError(it)
                }
                .collect {
                    setLoading(false)
                    when (it) {
                        is BaseResult.Success -> onOrdersLoaded(it.data)
                        is BaseResult.Errors -> onOrdersErrorLoad(it.error)
                    }
                }
        }
    }

}

sealed class OrdersActivityState {
    data class Loading(val isLoading: Boolean) : OrdersActivityState()
    data class Error(val throwable: Throwable) : OrdersActivityState()
    data class OrdersLoaded(val ordersEntity: List<OrderEntity>) : OrdersActivityState()
    data class OrdersErrorLoad(val response: WrappedListResponse<OrderResponse>) :
        OrdersActivityState()
}