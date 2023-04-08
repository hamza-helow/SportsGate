package com.souqApp.domain.checkout_details

import com.souqApp.data.checkout_details.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutRequest
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaymentDetailsUseCase @Inject constructor(private val paymentDetailsRepository: PaymentDetailsRepository) {

    suspend fun getCheckoutDetails(deliveryOptionId: Int?): Flow<BaseResult<CheckoutDetailsEntity, WrappedResponse<CheckoutDetailsResponse>>> {
        return paymentDetailsRepository.getCheckoutDetails(deliveryOptionId)
    }

    suspend fun checkCouponCode(couponCode: String): Flow<Boolean> {
        return paymentDetailsRepository.checkCouponCode(couponCode)
    }

    suspend fun checkout(
        couponCode: String?,
        addressId: Int?,
        deliveryOptionId: Int?
    ): Flow<BaseResult<CheckoutEntity, WrappedResponse<CheckoutResponse>>> {
        return paymentDetailsRepository.checkout(couponCode, addressId, deliveryOptionId)
    }
}