package com.souqApp.domain.orders

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.orders.remote.OrderResponse
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {

    suspend fun getOrders(): Flow<BaseResult<List<OrderEntity>, WrappedListResponse<OrderResponse>>>
}