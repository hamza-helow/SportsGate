package com.souqApp.domain.orders

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.orders.remote.dto.OrderDetailsResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.orders.entity.OrderDetailsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderDetailsUseCase @Inject constructor(private val orderRepository: OrdersRepository) {

    suspend fun getOrderDetails(orderId: Int): Flow<BaseResult<OrderDetailsEntity, WrappedResponse<OrderDetailsResponse>>> {
        return orderRepository.getOrderDetails(orderId)
    }
}