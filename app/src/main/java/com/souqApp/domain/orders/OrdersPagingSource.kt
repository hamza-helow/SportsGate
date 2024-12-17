package com.souqApp.domain.orders

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.orders.entity.OrderEntity

class OrdersPagingSource(private val ordersRepository: OrdersRepository) :
    PagingSource<Int, OrderEntity>() {

    override fun getRefreshKey(state: PagingState<Int, OrderEntity>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OrderEntity> {
        val pageNumber = params.key ?: 1
        val response = ordersRepository.getOrders()
        var orders: List<OrderEntity> = emptyList()

        response.collect {
            when (it) {
                is BaseResult.Errors -> Unit
                is BaseResult.Success -> {
                    orders = it.data
                }
            }
        }

        return LoadResult.Page(
            data = orders,
            prevKey = if (pageNumber == 1) null else (pageNumber - 1),
            nextKey = if (pageNumber== 1) null else (pageNumber + 1)
        )
    }
}