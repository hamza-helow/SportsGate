package com.souqApp.domain.wish_list

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow

interface WishListRepository {

    suspend fun getAll(): Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductEntity>>>
}