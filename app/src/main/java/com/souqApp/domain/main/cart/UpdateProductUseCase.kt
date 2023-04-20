package com.souqApp.domain.main.cart

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.UpdateProductCartResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.entity.ProductInCartEntity
import com.souqApp.domain.main.cart.entity.UpdateProductCartEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(private val cartRepository: CartRepository) {

    suspend fun execute(
        product: ProductInCartEntity,
        isIncrease: Boolean
    ): Flow<BaseResult<UpdateProductCartEntity, WrappedResponse<UpdateProductCartResponse>>> {

        return if (product.qty == 1 && isIncrease.not())
            cartRepository.deleteProductFromCart(product.cartItemId)

        else {
            val qty = if (isIncrease) product.qty + 1 else product.qty - 1
            cartRepository.updateProductQty(product.id, qty, product.combinationId)
        }
    }
}