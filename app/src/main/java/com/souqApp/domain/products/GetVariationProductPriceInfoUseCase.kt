package com.souqApp.domain.products

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.products.remote.dto.VariationProductPriceInfoResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.entity.VariationProductPriceInfoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVariationProductPriceInfoUseCase @Inject constructor(private val productsRepository: ProductsRepository) {

    suspend fun execute(
        productId: Int,
        label: String
    ): Flow<BaseResult<VariationProductPriceInfoEntity, WrappedResponse<VariationProductPriceInfoResponse>>> {
        return productsRepository.getVariationProductPriceInfo(productId, label)
    }
}