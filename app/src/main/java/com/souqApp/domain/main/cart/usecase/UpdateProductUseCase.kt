package com.souqApp.domain.main.cart.usecase

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.UpdateProductCartResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.CartRepository
import com.souqApp.domain.main.cart.entity.ProductInCartEntity
import com.souqApp.domain.main.cart.entity.UpdateProductCartEntity
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.infra.utils.getTimestampInSeconds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val sharedPrefs: SharedPrefs
) {
    suspend fun execute(
        product: ProductInCartEntity,
        isIncrease: Boolean
    ): Flow<BaseResult<UpdateProductCartEntity, WrappedResponse<UpdateProductCartResponse>>> {
        return flow {
            val productId = product.cartItemId
            val qty = if (isIncrease) product.qty + 1 else product.qty - 1
            val combinationId = product.combinationId

            val response =
                if (product.qty == 1 && isIncrease.not())
                    cartRepository.deleteProductFromCart(productId)
                else
                    cartRepository.updateProductQty(productId, qty, combinationId)

            if (response.status) {
                sharedPrefs.setLastCartUpdateTimeStamp(getTimestampInSeconds())
                emit(BaseResult.Success(data = response.data.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}