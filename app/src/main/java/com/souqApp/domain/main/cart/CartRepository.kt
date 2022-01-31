package com.souqApp.domain.main.cart

import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutRequest
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.entity.CartDetailsEntity
import com.souqApp.domain.main.cart.entity.CheckoutDetailsEntity
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CartRepository {

    suspend fun getCartDetails(): Flow<BaseResult<CartDetailsEntity, WrappedResponse<CartDetailsResponse>>>

    suspend fun getCheckoutDetails(): Flow<BaseResult<CheckoutDetailsEntity, WrappedResponse<CheckoutDetailsResponse>>>

    suspend fun checkout(checkoutRequest: CheckoutRequest): Flow<BaseResult<CheckoutEntity, WrappedResponse<CheckoutResponse>>>

    suspend fun deleteProductFromCart(productId: Int): Flow<BaseResult<Nothing, WrappedResponse<Nothing>>>

    suspend fun updateProductQty(
        productId: Int,
        qty: Int
    ): Flow<BaseResult<Nothing, WrappedResponse<Nothing>>>


    suspend fun checkCouponCode(couponCode: String): Flow<BaseResult<Nothing, WrappedResponse<Nothing>>>
}