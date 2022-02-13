package com.souqApp.domain.order_details

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.order_details.remote.OrderDetailsEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow

interface OrderDetailsRepository {

    suspend fun getOrderDetails(orderId: Int): Flow<BaseResult<OrderDetailsEntity, WrappedResponse<OrderDetailsEntity>>>
}