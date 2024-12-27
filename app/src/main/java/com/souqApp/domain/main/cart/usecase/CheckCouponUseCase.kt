package com.souqApp.domain.main.cart.usecase

import com.souqApp.domain.main.cart.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckCouponUseCase @Inject constructor(private val cartRepository: CartRepository) {

    suspend fun execute(couponCode: String): Flow<Boolean> {
        return flow {
            val response = cartRepository.checkCouponCode(couponCode)
            emit(response.status)
        }
    }

}