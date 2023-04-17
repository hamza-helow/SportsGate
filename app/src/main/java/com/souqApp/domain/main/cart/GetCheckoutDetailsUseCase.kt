package com.souqApp.domain.main.cart

import com.souqApp.data.main.cart.remote.dto.CheckoutDetailsResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.domain.common.BaseResult
import com.souqApp.domain.main.cart.entity.CheckoutDetailsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCheckoutDetailsUseCase @Inject constructor(private val cartRepository: CartRepository) {

    suspend fun execute(deliveryOptionId: Int?): Flow<BaseResult<CheckoutDetailsEntity, WrappedResponse<CheckoutDetailsResponse>>> {
        return cartRepository.getCheckoutDetails(deliveryOptionId)
    }

}