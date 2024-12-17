package com.souqApp.domain.orders

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.notification.remote.NotificationEntity
import com.souqApp.data.orders.remote.OrderResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.notification.NotificationsPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrdersUseCase @Inject constructor(private val ordersRepository: OrdersRepository) {

    fun invoke(): LiveData<PagingData<OrderEntity>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { OrdersPagingSource(ordersRepository) }
    ).liveData
}