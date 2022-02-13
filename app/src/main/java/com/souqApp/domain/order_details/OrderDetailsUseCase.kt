package com.souqApp.domain.order_details

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.order_details.remote.OrderDetailsEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderDetailsUseCase @Inject constructor(private val orderDetailsRepository: OrderDetailsRepository) {

    suspend fun getOrderDetails(orderId: Int): Flow<BaseResult<OrderDetailsEntity, WrappedResponse<OrderDetailsEntity>>> {
        return orderDetailsRepository.getOrderDetails(orderId)
    }
}