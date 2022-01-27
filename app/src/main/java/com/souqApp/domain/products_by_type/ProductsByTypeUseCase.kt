package com.souqApp.domain.products_by_type

import com.souqApp.data.products_by_type.remote.dto.ProductsByTypeRequest
import com.souqApp.data.products_by_type.remote.dto.ProductsByTypeResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductsByTypeUseCase @Inject constructor(private val productsByTypeRepository: ProductsByTypeRepository) {
    suspend fun getProductsByType(request: ProductsByTypeRequest): Flow<BaseResult<ProductsByTypeEntity, WrappedResponse<ProductsByTypeResponse>>> {
        return productsByTypeRepository.getProductsByType(request)
    }
}