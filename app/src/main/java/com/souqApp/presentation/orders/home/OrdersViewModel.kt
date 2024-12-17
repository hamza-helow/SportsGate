package com.souqApp.presentation.orders.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.notification.remote.NotificationEntity
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
class OrdersViewModel @Inject constructor(ordersUseCase: OrdersUseCase) : ViewModel() {
    val orders: LiveData<PagingData<OrderEntity>> = ordersUseCase.invoke().cachedIn(viewModelScope)
}