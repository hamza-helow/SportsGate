package com.souqApp.domain.main.cart

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.main.cart.remote.dto.CheckoutResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.entity.CheckoutEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckoutUseCase @Inject constructor(private val cartRepository: CartRepository) {

    suspend fun execute(
        couponCode: String?,
        addressId: Int?,
        deliveryOptionId: Int?
    ): Flow<BaseResult<CheckoutEntity, WrappedResponse<CheckoutResponse>>> {

        return flow {
            val response = cartRepository.checkout(couponCode, addressId, deliveryOptionId)
            if (response.status) {
                emit(BaseResult.Success(data = response.data.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }
}