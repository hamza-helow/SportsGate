package com.souqApp.domain.main.cart

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(private val cartRepository: CartRepository) {

    suspend fun execute(productId: Int): Flow<Boolean> {
        return cartRepository.deleteProductFromCart(productId)
    }

}