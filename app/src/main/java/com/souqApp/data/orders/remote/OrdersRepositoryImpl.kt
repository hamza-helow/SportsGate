package com.souqApp.data.orders.remote

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.domain.common.BaseResult
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
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                val data = response.body()!!.data!!
                emit(BaseResult.Success(data = data.toEntity()))
            } else {
                emit(BaseResult.Errors(response.body()!!))
            }

        }
    }
}