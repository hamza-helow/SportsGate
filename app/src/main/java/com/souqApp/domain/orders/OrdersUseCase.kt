package com.souqApp.domain.orders

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.souqApp.domain.orders.entity.OrderEntity
import javax.inject.Inject

class OrdersUseCase @Inject constructor(private val ordersRepository: OrdersRepository) {

    fun invoke(): LiveData<PagingData<OrderEntity>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { OrdersPagingSource(ordersRepository) }
    ).liveData
}