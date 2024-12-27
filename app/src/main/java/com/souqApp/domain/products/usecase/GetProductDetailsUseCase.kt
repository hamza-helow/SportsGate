package com.souqApp.domain.products.usecase

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.products.remote.dto.ProductDetailsEntity
import com.souqApp.data.products.remote.dto.ProductDetailsResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(private val productsRepository: ProductsRepository) {

    suspend fun invoke(productID: Int?): Flow<BaseResult<ProductDetailsEntity, WrappedResponse<ProductDetailsResponse>>> {
        return flow {
            val response = productsRepository.getProductDetails(productID)
            val isSuccessful = response.status
            if (isSuccessful) {
                emit(BaseResult.Success(response.data.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

}