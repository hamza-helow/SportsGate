package com.souqApp.domain.products.usecase

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.products.remote.dto.VariationProductPriceInfoResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.ProductsRepository
import com.souqApp.domain.products.entity.VariationProductPriceInfoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetVariationProductPriceInfoUseCase @Inject constructor(private val productsRepository: ProductsRepository) {

    suspend fun execute(
        productId: Int,
        label: String
    ): Flow<BaseResult<VariationProductPriceInfoEntity, WrappedResponse<VariationProductPriceInfoResponse>>> {
        return flow {
            val response = productsRepository.getVariationProductPriceInfo(productId, label)
            if (response.status) {
                emit(BaseResult.Success(response.data.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}