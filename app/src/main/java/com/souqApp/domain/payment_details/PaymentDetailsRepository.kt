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

interface PaymentDetailsRepository {

    suspend fun getCheckoutDetails(): Flow<BaseResult<CheckoutDetailsEntity, WrappedResponse<CheckoutDetailsResponse>>>

    suspend fun getAddresses(): Flow<BaseResult<List<AddressEntity>, WrappedListResponse<AddressResponse>>>

    suspend fun checkout(checkoutRequest: CheckoutRequest): Flow<BaseResult<CheckoutEntity, WrappedResponse<CheckoutResponse>>>

    suspend fun checkCouponCode(couponCode: String): Flow<Boolean>

}