package com.souqApp.domain.products

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    suspend fun getProducts(categoryId: Int): Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductEntity>>>
}