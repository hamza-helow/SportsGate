package com.souqApp.domain.main.cart

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckCouponUseCase @Inject constructor(private val cartRepository: CartRepository) {

    suspend fun execute(couponCode: String): Flow<Boolean> {
        return cartRepository.checkCouponCode(couponCode)
    }

}