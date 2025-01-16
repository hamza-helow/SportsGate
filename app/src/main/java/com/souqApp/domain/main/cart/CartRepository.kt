package com.souqApp.domain.main.cart

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.data.main.cart.remote.dto.PaymentMethodResponse
import com.souqApp.data.main.cart.remote.dto.UpdateProductCartResponse

interface CartRepository {

    suspend fun getCartDetails(updated: Long): WrappedResponse<CartDetailsResponse>

    suspend fun deleteProductFromCart(cartItemId: Int): WrappedResponse<UpdateProductCartResponse>

    suspend fun updateProductQty(
        productId: Int,
        qty: Int,
        combinationId: Int?
    ): WrappedResponse<UpdateProductCartResponse>

    suspend fun getCheckoutDetails(
        deliveryOptionId: Int?,
        updated: Long
    ): WrappedResponse<CheckoutDetailsResponse>


    suspend fun checkout(
        couponCode: String?,
        addressId: Int?,
        deliveryOptionId: Int?,
        paymentMethodId: Int?
    ): WrappedResponse<CheckoutResponse>

    suspend fun checkCouponCode(couponCode: String): WrappedResponse<Nothing>

    suspend fun resetCart(): WrappedResponse<Nothing>

    suspend fun getPaymentMethods(): WrappedListResponse<PaymentMethodResponse>

}