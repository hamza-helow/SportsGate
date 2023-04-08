package com.souqApp.domain.product_details

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.product_details.remote.VariationProductPriceInfoResponse
import com.souqApp.domain.common.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVariationProductPriceInfoUseCase @Inject constructor(private val productDetailsRepository: ProductDetailsRepository) {

    suspend fun execute(
        productId: Int,
        label: String
    ): Flow<BaseResult<VariationProductPriceInfoEntity, WrappedResponse<VariationProductPriceInfoResponse>>> {
        return productDetailsRepository.getVariationProductPriceInfo(productId, label)
    }
}