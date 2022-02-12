package com.souqApp.data.orders.remote

import com.souqApp.data.common.utlis.WrappedListResponse
import retrofit2.Response
import retrofit2.http.GET

interface OrdersApi {
    @GET("v1/users/orders/getOrders")
    suspend fun getOrders(): Response<WrappedListResponse<OrderResponse>>
}
