package com.souqApp.domain.main.cart

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.UpdateProductQtyResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import com.souqApp.domain.main.cart.entity.UpdateProductQtyEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartUseCase @Inject constructor(private val cartRepository: CartRepository) {


    suspend fun getCartDetails(): Flow<BaseResult<CartDetailsEntity, WrappedResponse<CartDetailsResponse>>> {
        return cartRepository.getCartDetails()
    }

    suspend fun deleteProductFromCart(productId: Int): Flow<Boolean> {
        return cartRepository.deleteProductFromCart(productId)
    }

    suspend fun updateProductQty(
        productId: Int,
        qty: Int
    ): Flow<BaseResult<UpdateProductQtyEntity, WrappedResponse<UpdateProductQtyResponse>>> {
        return cartRepository.updateProductQty(productId, qty)
    }


    suspend fun checkCouponCode(couponCode: String): Flow<BaseResult<Nothing, WrappedResponse<Nothing>>> {
        return cartRepository.checkCouponCode(couponCode)
    }
}