package com.souqApp.data.orders.remote

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.orders.OrderDetailsEntity
import com.souqApp.domain.orders.OrderEntity
import com.souqApp.domain.orders.OrdersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(private val ordersApi: OrdersApi) :
    OrdersRepository {
    override suspend fun getOrders(): Flow<BaseResult<List<OrderEntity>, WrappedListResponse<OrderResponse>>> {
        return flow {
            val response = ordersApi.getOrders()

            if (response.status) {
                emit(BaseResult.Success(data = response.data.orEmpty().toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

    override suspend fun getOrderDetails(orderId: Int): Flow<BaseResult<OrderDetailsEntity, WrappedResponse<OrderDetailsResponse>>> {
        return flow {
            val response = ordersApi.getOrderDetails(order_id = orderId)
            if (response.status) {
                emit(BaseResult.Success(response.data.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}