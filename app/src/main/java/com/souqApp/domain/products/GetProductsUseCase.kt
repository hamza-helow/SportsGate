package com.souqApp.domain.products

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val productsRepository: ProductsRepository) {
    suspend fun execute(categoryId:Int): Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductEntity>>> {
       return productsRepository.getProducts(categoryId)
    }
}