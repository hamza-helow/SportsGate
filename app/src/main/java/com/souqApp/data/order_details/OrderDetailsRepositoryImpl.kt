package com.souqApp.data.order_details

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.order_details.remote.OrderDetailsApi
import com.souqApp.data.order_details.remote.OrderDetailsEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.order_details.OrderDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrderDetailsRepositoryImpl @Inject constructor(private val orderDetailsApi: OrderDetailsApi) :
    OrderDetailsRepository {
    override suspend fun getOrderDetails(orderId: Int): Flow<BaseResult<OrderDetailsEntity, WrappedResponse<OrderDetailsEntity>>> {
        return flow {

            val response = orderDetailsApi.getOrderDetails(order_id = orderId)
            val isSuccessful = response.body()?.status
            if (isSuccessful == true) {
                val data = response.body()!!.data!!
                emit(BaseResult.Success(data))

            } else {
                emit(BaseResult.Errors(response.body()!!))
            }

        }
    }
}