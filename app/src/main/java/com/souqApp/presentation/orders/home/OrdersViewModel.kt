package com.souqApp.presentation.orders.home

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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(private val ordersUseCase: OrdersUseCase) : ViewModel() {

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val orderLiveData: MutableLiveData<BaseResult<List<OrderEntity>, WrappedListResponse<OrderResponse>>> =
        MutableLiveData()

    private fun setLoading(isLoading: Boolean) {
        loadingLiveData.value = isLoading
    }


    @Inject
    fun getOrders() {
        viewModelScope.launch {

            ordersUseCase
                .getOrders()
                .onStart { setLoading(true) }
                .catch { setLoading(false) }
                .collect {
                    setLoading(false)
                    orderLiveData.value = it
                }
        }
    }

}