package com.souqApp.domain.main.cart

import com.souqApp.data.main.cart.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.*
import com.souqApp.domain.main.cart.entity.CheckoutDetailsEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import com.souqApp.domain.main.cart.entity.UpdateProductCartEntity
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    suspend fun getCartDetails(): Flow<BaseResult<CartDetailsEntity, WrappedResponse<CartDetailsResponse>>>

    suspend fun deleteProductFromCart(cartItemId: Int): Flow<BaseResult<UpdateProductCartEntity, WrappedResponse<UpdateProductCartResponse>>>

    suspend fun updateProductQty(
        productId: Int,
        qty: Int,
        combinationId: Int?
    ): Flow<BaseResult<UpdateProductCartEntity, WrappedResponse<UpdateProductCartResponse>>>

    suspend fun getCheckoutDetails(deliveryOptionId: Int?): Flow<BaseResult<CheckoutDetailsEntity, WrappedResponse<CheckoutDetailsResponse>>>


    suspend fun checkout(
        couponCode: String?,
        addressId: Int?,
        deliveryOptionId: Int?
    ): Flow<BaseResult<CheckoutEntity, WrappedResponse<CheckoutResponse>>>

    suspend fun checkCouponCode(couponCode: String): Flow<Boolean>

}