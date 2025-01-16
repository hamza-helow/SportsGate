package com.souqApp.data.main.cart.remote

import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CartDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.data.main.cart.remote.dto.PaymentMethodResponse
import com.souqApp.data.main.cart.remote.dto.UpdateProductCartResponse
import com.souqApp.domain.main.cart.CartRepository
import retrofit2.http.Query
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val cartApi: CartApi) : CartRepository {

    override suspend fun getCartDetails(updated: Long): WrappedResponse<CartDetailsResponse> {
        return cartApi.getCartDetails(updated)
    }


    override suspend fun deleteProductFromCart(cartItemId: Int): WrappedResponse<UpdateProductCartResponse> {
        return cartApi.deleteProductFromCart(cartItemId)
    }

    override suspend fun updateProductQty(
        productId: Int,
        qty: Int,
        combinationId: Int?
    ): WrappedResponse<UpdateProductCartResponse> {
        return cartApi.updateProductQty(productId, qty, combinationId)
    }


    override suspend fun getCheckoutDetails(
        deliveryOptionId: Int?,
        updated: Long
    ): WrappedResponse<CheckoutDetailsResponse> {
        return cartApi.getCheckoutDetails(deliveryOptionId, updated)
    }


    override suspend fun checkout(
        couponCode: String?,
        addressId: Int?,
        deliveryOptionId: Int?,
        paymentMethodId: Int?
    ): WrappedResponse<CheckoutResponse> {
        return cartApi.checkout(couponCode, addressId, deliveryOptionId, paymentMethodId)
    }

    override suspend fun checkCouponCode(couponCode: String): WrappedResponse<Nothing> {
        return cartApi.checkCouponCode(couponCode)
    }

    override suspend fun resetCart(): WrappedResponse<Nothing> {
        return cartApi.resetCart()
    }

    override suspend fun getPaymentMethods(): WrappedListResponse<PaymentMethodResponse> {
        return cartApi.getPaymentMethods()
    }
}


