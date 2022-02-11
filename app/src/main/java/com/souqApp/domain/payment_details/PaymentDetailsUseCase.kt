package com.souqApp.domain.payment_details

import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutRequest
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.data.payment_details.remote.dto.CheckoutDetailsResponse
import com.souqApp.domain.addresses.AddressEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaymentDetailsUseCase @Inject constructor(private val paymentDetailsRepository: PaymentDetailsRepository) {

    suspend fun getCheckoutDetails(): Flow<BaseResult<CheckoutDetailsEntity, WrappedResponse<CheckoutDetailsResponse>>> {
        return paymentDetailsRepository.getCheckoutDetails()
    }

    suspend fun getAddresses(): Flow<BaseResult<List<AddressEntity>, WrappedListResponse<AddressResponse>>> {
        return paymentDetailsRepository.getAddresses()
    }

    suspend fun checkCouponCode(couponCode: String): Flow<Boolean> {
        return paymentDetailsRepository.checkCouponCode(couponCode)
    }

    suspend fun checkout(checkoutRequest: CheckoutRequest): Flow<BaseResult<CheckoutEntity, WrappedResponse<CheckoutResponse>>> {
        return paymentDetailsRepository.checkout(checkoutRequest)
    }
}