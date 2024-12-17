package com.souqApp.domain.orders

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.orders.remote.dto.OrderDetailsResponse
import com.souqApp.data.orders.remote.dto.OrderResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.orders.entity.OrderDetailsEntity
import com.souqApp.domain.orders.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {

    suspend fun getOrders(): Flow<BaseResult<List<OrderEntity>, WrappedListResponse<OrderResponse>>>

    suspend fun getOrderDetails(orderId: Int): Flow<BaseResult<OrderDetailsEntity, WrappedResponse<OrderDetailsResponse>>>

}