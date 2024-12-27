package com.souqApp.domain.products.usecase

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.products.remote.dto.AddProductToCartResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.products.entity.AddProductToCartEntity
import com.souqApp.domain.products.ProductsRepository
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.infra.utils.getTimestampInSeconds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddProductToCartUseCase @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val sharedPrefs: SharedPrefs
) {
    suspend fun invoke(
        productId: Int,
        combinationId: Int?
    ): Flow<BaseResult<AddProductToCartEntity, WrappedResponse<AddProductToCartResponse>>> {

        return flow {
            val response = productsRepository.addProductToCart(productId, combinationId)
            if (response.status) {
                sharedPrefs.setLastCartUpdateTimeStamp(getTimestampInSeconds())
                emit(BaseResult.Success(response.data.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}