package com.souqApp.domain.products.usecase

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.main.home.remote.dto.ProductEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFavoriteProductsUseCase @Inject constructor(private val productsRepository: ProductsRepository) {

    suspend fun invoke(): Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductEntity>>> {
        return flow {
            val response = productsRepository.getFavoriteProducts()
            if (response.status) {
                emit(BaseResult.Success(response.data.orEmpty()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

}