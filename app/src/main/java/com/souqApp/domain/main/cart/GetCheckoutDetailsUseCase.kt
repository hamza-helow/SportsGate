package com.souqApp.domain.main.cart

import com.souqApp.data.common.mapper.toEntity
import com.souqApp.data.main.cart.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.entity.CheckoutDetailsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCheckoutDetailsUseCase @Inject constructor(private val cartRepository: CartRepository) {
    suspend fun execute(deliveryOptionId: Int?): Flow<BaseResult<CheckoutDetailsEntity, WrappedResponse<CheckoutDetailsResponse>>> {
        return flow {
            val response = cartRepository.getCheckoutDetails(deliveryOptionId)
            if (response.status) {
                emit(BaseResult.Success(data = response.data.toEntity()))
            } else {
                emit(BaseResult.Errors(response))
            }
        }
    }

}