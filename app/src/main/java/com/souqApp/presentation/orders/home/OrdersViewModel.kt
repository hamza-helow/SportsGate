package com.souqApp.presentation.orders.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.souqApp.domain.orders.entity.OrderEntity
import com.souqApp.domain.orders.OrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(ordersUseCase: OrdersUseCase) : ViewModel() {
    val orders: LiveData<PagingData<OrderEntity>> = ordersUseCase.invoke().cachedIn(viewModelScope)
}