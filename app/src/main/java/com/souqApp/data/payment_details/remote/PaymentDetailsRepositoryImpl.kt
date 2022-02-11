package com.souqApp.data.payment_details.remote

import com.souqApp.data.addresses.remote.dto.AddressResponse
import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutRequest
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.data.payment_details.remote.dto.CheckoutDetailsResponse
import com.souqApp.domain.addresses.AddressEntity
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import com.souqApp.domain.payment_details.CheckoutDetailsEntity
import com.souqApp.domain.payment_details.PaymentDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PaymentDetailsRepositoryImpl @Inject constructor(
    private val paymentDetailsApi: PaymentDetailsApi
) :
    PaymentDetailsRepository {

    override suspend fun getCheckoutDetails(): Flow<BaseResult<CheckoutDetailsEntity, WrappedResponse<CheckoutDetailsResponse>>> {

        return flow {
            val response = paymentDetailsApi.getCheckoutDetails()
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                val data = response.body()!!.data!!
                emit(BaseResult.Success(data = data.toEntity()))

            } else {
                emit(BaseResult.Errors(response.body()!!))
            }

        }
    }

    override suspend fun getAddresses(): Flow<BaseResult<List<AddressEntity>, WrappedListResponse<AddressResponse>>> {
        return flow {
            val response = paymentDetailsApi.getAddresses()
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                val data = response.body()!!.data!!.toEntity()
                emit(BaseResult.Success(data))
            } else {
                emit(BaseResult.Errors(response.body()!!))
            }
        }
    }

    override suspend fun checkout(checkoutRequest: CheckoutRequest): Flow<BaseResult<CheckoutEntity, WrappedResponse<CheckoutResponse>>> {
        return flow {

            val response = paymentDetailsApi.checkout(checkoutRequest)
            val isSuccessful = response.body()?.status

            if (isSuccessful == true) {
                val data = response.body()!!.data!!
                emit(BaseResult.Success(data = data.toEntity()))

            } else {
                emit(BaseResult.Errors(response.body()!!))
            }
        }
    }

    override suspend fun checkCouponCode(couponCode: String): Flow<Boolean> {
        return flow {
            val response = paymentDetailsApi.checkCouponCode(couponCode)
            val isSuccessful = response.body()?.status
            emit(isSuccessful == true)
        }
    }

}